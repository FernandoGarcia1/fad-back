package com.fad.fad.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fad.fad.security.jwt.JwtEntryPoint;
import com.fad.fad.security.jwt.JwtFilter;
import com.fad.fad.security.jwt.JwtProvider;

@Configuration
public class WebSecurity {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling().authenticationEntryPoint(jwtEntryPoint);
        http.addFilterBefore(new JwtFilter(), BasicAuthenticationFilter.class);
        http.authenticationProvider(jwtProvider);
        return http.build();
    }
}