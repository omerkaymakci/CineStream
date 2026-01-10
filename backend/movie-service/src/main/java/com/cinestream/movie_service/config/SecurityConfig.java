package com.cinestream.movie_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Varsayılan olarak CSRF kapalı, gateway arkasında olduğumuz için
                .csrf(csrf -> csrf.disable())
                // Tüm endpoint’ler authenticated olmasa bile gateway zaten auth yapıyor
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // İstekleri gateway’e bırakıyoruz
                );

        return http.build();
    }
}

