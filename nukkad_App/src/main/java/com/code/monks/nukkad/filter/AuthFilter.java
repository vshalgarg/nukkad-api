//package com.code.monks.nukkad.filter;
//
//import com.code.monks.nukkad.client.AuthRestClient;
//import com.code.monks.nukkad.context.RequestContextHolder;
//import com.code.monks.nukkad.dto.Customer;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.tomcat.util.http.fileupload.RequestContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.sql.rowset.serial.SerialException;
//import java.io.IOException;
//
//@Component
//public class AuthFilter extends OncePerRequestFilter{
//
//    private final AuthRestClient authRestClient;
//
//    @Autowired
//    public AuthFilter(AuthRestClient authRestClient){
//        this.authRestClient = authRestClient;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response, FilterChain filterChain) throws ServletException , IOException{
//        try{
//            String authHeader = request.getHeader("Authorization");
//
//            if(authHeader != null && authHeader.startsWith("Bearer ")){
//                String token  = authHeader.substring(7);
//
//                Customer customer = authRestClient.validateToken(token);
//
//                if(customer != null){
//                    RequestContextHolder.setCustomer(customer);
//                }else{
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    response.getWriter().write("Invalid token");
//                    return;
//                }
//            }else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Missing Authorization header");
//                return;
//            }
//            filterChain.doFilter(request,response);
//        }finally {
//            RequestContextHolder.clear();
//        }
//    }
//}
