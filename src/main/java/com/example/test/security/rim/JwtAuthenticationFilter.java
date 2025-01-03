package com.example.test.security.rim;

import com.example.test.exception.AuthException;
import com.example.test.util.rim.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        log.debug("JWT 필터 시작: {}", request.getRequestURI());
        
        // 공개 URL인 경우 토큰 검사 스킵
        if (isPublicUrl(request.getRequestURI())) {
            log.debug("공개 URL: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        
        // 1. 쿠키에서 JWT 토큰 추출
        String token = extractTokenFromCookie(request);
        
        if (token != null) {
            try {
                // 2. 토큰 검증 및 사용자 정보 설정
                String userId = jwtUtil.validateTokenAndGetUserId(token);
                
                // 3. UserDetails 로드
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                
                // 4. Authentication 객체 생성 및 SecurityContext에 저장
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT 인증 성공: {}", userId);
                
            } catch (AuthException e) {
                // AuthException만 간단히 로깅
                log.debug("JWT 인증 실패: {}", e.getMessage());
            } catch (Exception e) {
                    log.info("JWT 인증 실패: {}", e.getMessage());
            }
        } else {
            log.debug("JWT 토큰 없음");
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
            url.equals("/api/auth/me") ||
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