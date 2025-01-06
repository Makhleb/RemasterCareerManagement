package com.example.test.config;

import com.example.test.security.rim.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created on 2024-12-26 by 최기환
 * Modified on 2024-12-27 by 구경림
 * Modified on 2024-01-02 by 구경림 - 쿠키 기반 인증 설정 추가
 */
@Configuration
@EnableAspectJAutoProxy
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 비활성화 (개발 환경)
        http.csrf(csrf -> csrf.disable());

        // 세션 설정
        http.sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // CORS 설정
        http.cors(cors -> cors
            .configurationSource(corsConfigurationSource())
        );

        http.authorizeHttpRequests(auth -> auth
            // 1. 공개 리소스
            .requestMatchers(
                "/",
                "/login",
                "/signup",
                "/company/signup",
                "/find-password",
                "/view/users/job-post/list",
                "/api/job-posts",
                "/api/job-posts/{id}",
                "/css/**",
                "/js/**",
                "/images/**",
                "/error",
                "/api/auth/**"
            ).permitAll()
            // 2. 이력서 관련 권한 설정
            .requestMatchers("/resume/register", "/resume/list").hasRole("USER")
            .requestMatchers("/resume/detail/**").hasAnyRole("USER", "COMPANY", "ADMIN")
            // 3. 나머지 설정들
            .requestMatchers("/api/users/**").hasAnyRole("USER", "COMPANY")
            .requestMatchers("/api/companies/**").hasRole("COMPANY")
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            // 마이페이지 권한 설정
            .requestMatchers("/user/mypage/**").hasRole("USER")
            // 4. 나머지는 인증 필요
            .anyRequest().authenticated()
        );

        // JWT 필터 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setMaxAge(3600L);

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
