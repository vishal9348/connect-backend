package com.vishal.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JwtFilter extends BasicAuthenticationFilter {

    private BeanFactory factory;

    public JwtFilter(AuthenticationManager authenticationManager, BeanFactory f){
        super(authenticationManager);
        factory=f;
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        String path = request.getRequestURI();
//
//        // Skip JWT check for swagger and OpenAPI endpoints
//        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") || path.startsWith("/api/v1/user/v3/api-docs") || path.startsWith("/api/v1/user/swagger-ui")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        if (header != null && header.startsWith("Bearer")){
//            JwtUtils utils=factory.getBean(JwtUtils.class);
//
//            String token=header.substring(7);
//            token=utils.validateToken(token);
//            JsonParser parser= JsonParserFactory.getJsonParser();
//            Map<String, Object> m=parser.parseMap(token);
//            String user= (String) m.get(JwtUtils.USER_CLAIM);
//            List<String> role= (List<String>) m.get(JwtUtils.ROLE_CLAIM);
//
//            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, AuthorityUtils.createAuthorityList(role));
//
//            SecurityContext context = SecurityContextHolder.getContext();
//            context.setAuthentication(auth);
//        }
//        chain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI();
        System.out.println("Request URI in JwtFilter: " + request.getRequestURI());


        // Skip JWT check for swagger and OpenAPI endpoints
        if (path.contains("/v3/api-docs") || path.contains("/swagger-ui")) {
            System.out.println("-------------------skipping---------------");
            chain.doFilter(request, response);
            return;
        }


        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            JwtUtils utils = factory.getBean(JwtUtils.class);

            String token = header.substring(7);
            token = utils.validateToken(token);
            JsonParser parser = JsonParserFactory.getJsonParser();
            Map<String, Object> m = parser.parseMap(token);
            String user = (String) m.get(JwtUtils.USER_CLAIM);
            List<String> role = (List<String>) m.get(JwtUtils.ROLE_CLAIM);

            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, AuthorityUtils.createAuthorityList(role));

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }

}
