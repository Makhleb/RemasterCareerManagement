package com.example.test.security;

import com.example.test.exception.AuthenticationException;
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
import com.example.test.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
/**
 * Created on 2024-12-30 by 구경림
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAspect {
    
    private final JwtUtil jwtUtil;

    @Before("@annotation(com.example.test.security.RequireToken)")
    public void validateToken(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

            String bearerToken = request.getHeader("Authorization");
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                throw new UnauthorizedException("인증 토큰이 필요합니다");
            }

            String token = jwtUtil.resolveToken(bearerToken);
            if (!jwtUtil.validateToken(token)) {
                throw new UnauthorizedException("유효하지 않은 토큰입니다");
            }

            String userId = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);

            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                    userId, 
                    null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
                );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            throw e;  // GlobalExceptionHandler에서 처리
        } catch (Exception e) {
            throw new AuthenticationException("인증 처리 중 오류가 발생했습니다", e);
        }
    }
} 