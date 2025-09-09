package com.code.monks.nukkad.filter;

import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.enums.RoleEnum;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class FirebaseAuthFilter extends OncePerRequestFilter {

    private final Environment environment;

    // Enable a mock user for local testing if needed
    private boolean isMockEnabled = false;

    public FirebaseAuthFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Skip auth endpoints
        return path.contains("/auth/verify-token");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                if (isMockEnabled && isLocalProfileActive()) {
                    log.warn("[FIREBASE AUTH FILTER] No token found. Injecting dummy user (LOCAL ONLY).");
                    User dummyUser = new User();
                    dummyUser.setId(403L);
                    dummyUser.setMobileNumber("9560121707");
                    dummyUser.setRoles(Collections.singletonList(RoleEnum.CUSTOMER));
                    UserContextHolder.setUser(dummyUser);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Token missing or invalid");
                    return;
                }
            } else {
                String idToken = authHeader.substring(7);
                FirebaseToken decodedToken;
                try {
                    decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                } catch (Exception e) {
                    log.error("[FIREBASE AUTH FILTER] Invalid token: {}", e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Invalid token");
                    return;
                }

                // Build user context
                User user = new User();
                user.setId(Long.parseLong(decodedToken.getUid())); // assuming UID is numeric
                user.setMobileNumber(decodedToken.getClaims().getOrDefault("phone_number", "").toString());
                user.setRoles(Collections.singletonList(RoleEnum.CUSTOMER)); // Default, or map claims if needed
                UserContextHolder.setUser(user);
                log.info("[FIREBASE AUTH FILTER] User authenticated: ID={}, Mobile={}", user.getId(), user.getMobileNumber());
            }

            filterChain.doFilter(request, response);

        } finally {
            UserContextHolder.clear();
        }
    }

    private boolean isLocalProfileActive() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("local".equals(profile)) return true;
        }
        return false;
    }
}
