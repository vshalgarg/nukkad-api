//package com.code.monks.nukkad.filter;
//
//import com.code.monks.nukkad.client.AuthRestClient;
//import com.code.monks.nukkad.context.RequestContextHolder;
//import com.code.monks.nukkad.dto.Customer;
//import com.code.monks.nukkad.exception.ExternalServiceException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class AuthFilter extends OncePerRequestFilter {
//
//    private final AuthRestClient authRestClient;
//
//    @Autowired
//    public AuthFilter(AuthRestClient authRestClient) {
//        this.authRestClient = authRestClient;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//        try {
//            String authHeader = request.getHeader("Authorization");
//
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Missing or invalid Authorization header");
//                return;
//            }
//
//            String token = authHeader.substring(7);
//
//            try {
//                Customer customer = authRestClient.validateToken(token);
//                RequestContextHolder.setCustomer(customer);
//            } catch (ExternalServiceException e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid token: " + e.getMessage());
//                return;
//            }
//
//
//            filterChain.doFilter(request, response);
//
//        } finally {
//
//            RequestContextHolder.clear();
//        }
//    }
//}
