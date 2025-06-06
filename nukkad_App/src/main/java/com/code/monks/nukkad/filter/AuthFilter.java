package com.code.monks.nukkad.filter;

import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.ExternalServiceException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final AuthRestClient authRestClient;

    @Autowired
    public AuthFilter(AuthRestClient authRestClient) {
        this.authRestClient = authRestClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            // ✅ TEST MODE: If no Authorization header is present, use a dummy user
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // Dummy mode using custom header
                String dummyRole = request.getHeader("X-Dummy-Role");

                User dummyUser = new User();
                dummyUser.setId(dummyRole != null && dummyRole.equalsIgnoreCase("STOREKEEPER") ? 2L : 2L);

                if ("STOREKEEPER".equalsIgnoreCase(dummyRole)) {
                    dummyUser.setRole(RoleEnum.STOREKEEPER);
                } else {
                    dummyUser.setRole(RoleEnum.CUSTOMER);
                }

                UserContextHolder.setUser(dummyUser);
            } else {
                String token = authHeader.substring(7);
                try {
                    User user = authRestClient.validateToken(token);
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
