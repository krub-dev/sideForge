package com.sideforge.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Sintaxis moderna, pero la antigua también funciona
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .anyRequest().permitAll()
                )
                .httpBasic();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        // You can add more users here if needed
        // UserDetails customer = User.withDefaultPasswordEncoder()
        //     .username("customer")
        //     .password("customer")
        //     .roles("CUSTOMER")
        //     .build();
        return new InMemoryUserDetailsManager(admin);
    }
}