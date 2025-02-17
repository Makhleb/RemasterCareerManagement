package com.example.test.security.rim;

import com.example.test.exception.AuthException;
import com.example.test.util.rim.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            // 쿠키에서 JWT 토큰 추출
            String token = extractTokenFromCookie(request);
            log.debug("Extracted token from cookie: {}", token != null ? "exists" : "null");

            if (token != null) {
                if (jwtUtil.validateToken(token)) {
                    // 토큰에서 사용자 정보 추출
                    Claims claims = jwtUtil.getAllClaimsFromToken(token);
                    String username = claims.getSubject();
                    String role = (String) claims.get("role");
                    String name = (String) claims.get("name");
                    
                    log.info("Token claims - username: {}, role: {}, name: {}", username, role, name);

                    // CustomUserDetails 생성
                    CustomUserDetails userDetails = CustomUserDetails.builder()
                        .username(username)
                        .role(role)
                        .name(name)
                        .isActive(true)
                        .build();

                    // Authentication 객체 생성 및 SecurityContext에 설정
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            Collections.singleton(new SimpleGrantedAuthority(role))
                        );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Authentication set in SecurityContext for user: {}", username);
                } else {
                    log.warn("Invalid JWT token found in cookie");
                }
            }
        } catch (Exception e) {
            log.error("JWT 토큰 처리 중 오류 발생: ", e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean isPublicUrl(String url) {
        return 
            url.equals("/") ||
            url.equals("/login") ||
            url.equals("/signup") ||
            url.equals("/api/auth/login") ||
            url.equals("/api/auth/signup") ||
            url.equals("/api/auth/logout") ||
            url.startsWith("/js/") ||
            url.startsWith("/css/") ||
            url.startsWith("/images/") ||
            url.startsWith("/fonts/") ||
            url.startsWith("/favicon");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/js/") ||
               path.startsWith("/css/") ||
               path.startsWith("/images/") ||
               path.startsWith("/fonts/") ||
               path.startsWith("/favicon");
    }
} 