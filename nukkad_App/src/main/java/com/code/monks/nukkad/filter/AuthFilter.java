package com.code.monks.nukkad.filter;

import com.code.monks.nukkad.auth.request.AuthTokenRequestDto;
import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.ExternalServiceException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final AuthRestClient authRestClient;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/send") || path.equals("/verify"); // add other paths as needed
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {

                User dummyUser = new User();
                dummyUser.setId(1L);
                dummyUser.setRoles(List.of(RoleEnum.CUSTOMER));

                UserContextHolder.setUser(dummyUser);
            }
            else {
                String token = authHeader.substring(7);
                try {
                    AuthTokenRequestDto authDto = new AuthTokenRequestDto();
                    authDto.setJwtToken(token);

                    User user = authRestClient.validateToken(authDto);
                    if (user != null) {
                        UserContextHolder.setUser(user);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid user");
                        return;
                    }
                } catch (ExternalServiceException e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid token: " + e.getMessage());
                    return;
                }
            }

            filterChain.doFilter(request, response);

        } finally {
            UserContextHolder.clear();
        }
    }
}
