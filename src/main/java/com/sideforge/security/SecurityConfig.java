package com.sideforge.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Basic config for HTTP Basic Auth with role-based access control
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Only for testing purposes
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .anyRequest().permitAll()
                )
                .httpBasic();

        return http.build();
    }
}
