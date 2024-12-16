package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // other configuration options
                .authorizeHttpRequests(authCustomizer -> authCustomizer
                        .requestMatchers(HttpMethod.POST, "/api/v1/books/").permitAll()
                        .requestMatchers("/api/v1/**").permitAll()
                )
                //.csrf().disable()
                .csrf(csrf -> csrf.disable())
                .build();
    }

}