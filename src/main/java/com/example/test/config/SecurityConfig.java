package com.example.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Created on 2024-12-26 by 최기환
 */
@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(auth -> auth.disable());
        http.formLogin(auth -> auth.disable());

        http
                .authorizeHttpRequests((auth) -> auth
                        // todo 회원가입/로그인 완료되면 퍼밋 수정하기!!!
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
