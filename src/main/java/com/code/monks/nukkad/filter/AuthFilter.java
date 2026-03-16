package com.code.monks.nukkad.filter;

import com.code.monks.nukkad.auth.request.AuthTokenRequestDto;
import com.code.monks.nukkad.client.AuthRestClient;
import com.code.monks.nukkad.constants.UrlConstants;
import com.code.monks.nukkad.context.UserContextHolder;
import com.code.monks.nukkad.dto.User;
import com.code.monks.nukkad.enums.RoleEnum;
import com.code.monks.nukkad.exception.ExternalServiceException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.code.monks.nukkad.constants.UrlConstants.ADMIN.LOGIN;
import static com.code.monks.nukkad.constants.UrlConstants.ADMIN.REGISTER;
import static com.code.monks.nukkad.constants.UrlConstants.OTP.VERIFY_OTP;

@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

    private final AuthRestClient authRestClient;
    private final Environment environment;

    @Value("${auth.mock.local:false}")
    private boolean isMockEnabled;

    public AuthFilter(AuthRestClient authRestClient, Environment environment) {
        this.authRestClient = authRestClient;
        this.environment = environment;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("[AUTH FILTER] request.getRequestURI() = {}", path);
        return path.contains("/otp/v1/otp/send/login") ||
                path.contains("/otp/v1/otp/verify/login") ||
                path.contains("/admin/v1/login") ||
                path.contains(UrlConstants.EXCEPTION_LOG.BASE + UrlConstants.EXCEPTION_LOG.ADD_EXCEPTION_LOG) ||
                path.contains(REGISTER) ||
                path.contains(LOGIN) ||
                path.contains(VERIFY_OTP) ||
                path.contains("/nukkad/api/admin/export-json");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                if (isMockEnabled && isLocalProfileActive()) {
                    log.warn("[AUTH FILTER] No token found. Injecting dummy user (LOCAL ONLY).");

                    User dummyUser = new User();
                    dummyUser.setId(403L);
                    dummyUser.setMobileNumber("9560121707");
                    dummyUser.setRoles(List.of(RoleEnum.STOREKEEPER));
                    UserContextHolder.setUser(dummyUser);

                } else {
                    log.warn("[AUTH FILTER] Missing or invalid token in non-local environment.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Token missing or invalid");
                    return;
                }
            }
            else {
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
        return Arrays.asList(activeProfiles).contains("local");
    }
}

