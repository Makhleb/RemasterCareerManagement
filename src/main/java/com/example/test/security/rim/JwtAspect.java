package com.example.test.security.rim;

import com.example.test.exception.UnauthorizedException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.example.test.util.rim.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.Cookie;
/**
 * Created on 2024-12-30 by 구경림
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAspect {
    
    private final JwtUtil jwtUtil;

    @Before("@annotation(com.example.test.security.rim.RequireToken)")
    public void validateToken(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

            Cookie[] cookies = request.getCookies();
            log.info("JWT 토큰 쿠키 확인 중");
            
            String token = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("JWT_TOKEN".equals(cookie.getName())) {
                        token = cookie.getValue();
                        log.info("쿠키에서 JWT 토큰을 찾았습니다");
                        break;
                    }
                }
            }
            
            if (token == null) {
                log.warn("쿠키에서 JWT 토큰을 찾을 수 없습니다");
                throw new UnauthorizedException("인증 토큰이 필요합니다");
            }

            if (!jwtUtil.validateToken(token)) {
                log.warn("유효하지 않은 JWT 토큰");
                throw new UnauthorizedException("유효하지 않은 토큰입니다");
            }

            String userId = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);
            log.info("사용자 토큰 검증 완료: 사용자 ID = {}, 권한 = {}", userId, role);
            
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    userId, 
                    null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
                );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("토큰 검증 성공 - 사용자: {}, 권한: {}", userId, role);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("토큰이 만료되었습니다");
        } catch (Exception e) {
            log.error("JWT 토큰 검증 오류", e);
            throw e;
        }
    }
} 