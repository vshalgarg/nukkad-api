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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final AuthRestClient authRestClient;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/send") || path.equals("/verify");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("[AUTH FILTER] No Authorization header or invalid format. Injecting dummy STOREKEEPER user");

                User dummyUser = new User();
                dummyUser.setId(2L);
                dummyUser.setMobileNumber("9560121707");
                dummyUser.setRoles(List.of(RoleEnum.CUSTOMER));
                UserContextHolder.setUser(dummyUser);

                log.info("[AUTH FILTER] Dummy user set: ID={}, Mobile={}, Roles={}",
                        dummyUser.getId(), dummyUser.getMobileNumber(), dummyUser.getRoles());

            } else {
                String token = authHeader.substring(7);
                log.info("[AUTH FILTER] Validating token...");

                AuthTokenRequestDto authDto = new AuthTokenRequestDto();
                authDto.setJwtToken(token);

                try {
                    User user = authRestClient.validateToken(authDto);

                    if (user != null && user.getId() != null) {
                        UserContextHolder.setUser(user);

                        log.info("[AUTH FILTER] User authenticated: ID={}, Mobile={}, Roles={}",
                                user.getId(), user.getMobileNumber(), user.getRoles());
                    } else {
                        log.warn("[AUTH FILTER] Token validation returned null or incomplete user.");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Unauthorized: Invalid user");
                        return;
                    }

                } catch (ExternalServiceException e) {
                    log.error("[AUTH FILTER] Token validation failed: {}", e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Invalid token");
                    return;
                }
            }

            filterChain.doFilter(request, response);

        } finally {
            log.debug("[AUTH FILTER] Clearing user context");
            UserContextHolder.clear();
        }
    }
}
