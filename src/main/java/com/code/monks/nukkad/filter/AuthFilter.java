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
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final AuthRestClient authRestClient;
    private final Environment environment;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/nukkad/api/otp/v1/send") || path.equals("/nukkad/api/otp/v1/verify");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                if (isLocalProfileActive()) {
                    log.warn("[AUTH FILTER] No Authorization header or invalid format. Injecting dummy STOREKEEPER user for local profile");

                    User dummyUser = new User();
                    dummyUser.setId(403L);
                    dummyUser.setMobileNumber("9560121707");
                    dummyUser.setRoles(List.of(RoleEnum.STOREKEEPER));
                    UserContextHolder.setUser(dummyUser);

                    log.info("[AUTH FILTER] Dummy user set: ID={}, Mobile={}, Roles={}",
                            dummyUser.getId(), dummyUser.getMobileNumber(), dummyUser.getRoles());
                } else {
                    log.warn("[AUTH FILTER] Missing or invalid token in non-local environment.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Token missing or invalid");
                    return;
                }

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

    private boolean isLocalProfileActive() {
        String[] activeProfiles = environment.getActiveProfiles();
        log.info("[AUTH FILTER] Active Spring Profiles: {}", Arrays.toString(activeProfiles));

        for (String profile : activeProfiles) {
            if ("local".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }
}
