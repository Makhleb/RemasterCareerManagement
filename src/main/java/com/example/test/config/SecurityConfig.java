package com.example.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import java.util.Arrays;

/**
 * Created on 2024-12-26 by 최기환
 * Modified on 2024-12-27 by 구경림
 * Modified on 2024-01-02 by 구경림 - 쿠키 기반 인증 설정 추가
 */
@Configuration
@EnableAspectJAutoProxy
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 개발 환경에서는 CSRF 비활성화 (나중에 운영 환경에서 활성화)
        if (isDevelopmentMode()) {
            http.csrf(csrf -> csrf.disable());
        } else {
            http.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            );
        }
        
        http.formLogin(auth -> auth.disable());

        // 쿠키 설정
        http.sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // CORS 설정
        http.cors(cors -> cors
            .configurationSource(corsConfigurationSource())
        );

        // 인증/인가 설정
        if (isDevelopmentMode()) {
            http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        } else {
            http.authorizeHttpRequests(auth -> auth
                // 공개 리소스
                .requestMatchers(
                    "/",
                    "/login",
                    "/signup",
                    "/api/auth/login",
                    "/api/auth/signup",
                    "/api/auth/logout",
                    "/api/auth/me",
                    "/js/**",
                    "/css/**",
                    "/images/**"
                ).permitAll()
                // API 엔드포인트 보호
                .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/companies/**").hasAnyRole("COMPANY", "ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            );
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // 쿠키 전송 허용
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(3600L); // preflight 캐시 시간

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 개발 모드 여부 확인
    private boolean isDevelopmentMode() {
        // application.properties에서 설정을 읽어오거나
        // 환경변수를 확인하여 개발 모드 여부 반환
        return true;  // 현재는 항상 개발 모드로 설정
    }
}
