package com.vishal.user_service.security;

import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ServletContext context;

    @Autowired
    private BeanFactory factory;

    @Bean
    public SecurityFilterChain getFilterChain(HttpSecurity http, AuthenticationManager manager) throws Exception{

        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(HttpMethod.POST, "/api/v1/user/authentication").permitAll()
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/api/v1/user/v3/api-docs",
                                        "/api/v1/user/swagger-ui.html",
                                        "/api/v1/user/swagger-ui/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/user").hasAnyRole("ADMIN","ANONYMOUS")
                                .anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .addFilterBefore(new JwtFilter(manager, factory), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration a) throws Exception{
        return a.getAuthenticationManager();
    }
}
