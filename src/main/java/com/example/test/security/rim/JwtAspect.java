package com.example.test.security.rim;

import com.example.test.exception.AuthException;
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
    private final SecurityUtil securityUtil;

    @Before("@annotation(requireToken)")
    public void checkToken(JoinPoint joinPoint, RequireToken requireToken) {
        // 1. 역할 체크
        String[] requiredRoles = requireToken.roles();
        if (requiredRoles.length > 0) {
            boolean hasAnyRole = false;
            for (String role : requiredRoles) {
                if (securityUtil.hasRole(role)) {
                    hasAnyRole = true;
                    break;
                }
            }
            if (!hasAnyRole) {
                throw AuthException.forbidden();
            }
        }

        // 2. 리소스 소유자 체크 (필요한 경우)
        if (requireToken.checkOwner()) {
            String resourceId = extractResourceId(joinPoint);
            if (!securityUtil.isResourceOwner(resourceId)) {
                throw AuthException.forbidden();
            }
        }
    }

    private String extractResourceId(JoinPoint joinPoint) {
        // 메서드 파라미터에서 리소스 ID 추출 로직
        return null;
    }
} 