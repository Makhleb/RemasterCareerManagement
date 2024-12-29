package com.example.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created on 2024-12-26 by 최기환
 * Modified on 2024-12-27 by 구경림
 */
@Configuration
@EnableAspectJAutoProxy
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(auth -> auth.disable());
        http.formLogin(auth -> auth.disable());

        // 개발 환경에서는 JWT 필터를 적용하지 않고 모든 요청 허용
        if (isDevelopmentMode()) {
            http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        } else {
            // 운영 환경에서는 JWT 인증 적용
            http
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
                );
        }
        
        return http.build();
    }

    // 개발 모드 여부 확인
    private boolean isDevelopmentMode() {
        // application.properties에서 설정을 읽어오거나
        // 환경변수를 확인하여 개발 모드 여부 반환
        return true;  // 현재는 항상 개발 모드로 설정
    }
}
