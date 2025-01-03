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
import org.springframework.security.core.Authentication;
import java.util.Map;
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

    @Before("execution(* com.example.test.service.rim.MainService.getMainPageData())")
    public void checkMainPageAccess(JoinPoint joinPoint) {
        log.info("메인 페이지 접근 권한 체크");
        
        try {
            Map<String, Object> userInfo = securityUtil.getCurrentUserInfo();
            String userType = (String) userInfo.get("type");
            
            // 유효한 사용자 타입인지 검증
            if (!isValidUserType(userType)) {
                throw AuthException.invalidUserType();
            }
            
            log.info("사용자 타입: {}", userType);
            
        } catch (Exception e) {
            // 권한 체크 실패 시 게스트로 처리
            SecurityContextHolder.getContext().setAuthentication(createGuestAuthentication());
        }
    }
    
    private boolean isValidUserType(String userType) {
        return userType != null && 
               (userType.equals("guest") || 
                userType.equals("user") || 
                userType.equals("company"));
    }
    
    private Authentication createGuestAuthentication() {
        return new UsernamePasswordAuthenticationToken(
            "guest",
            null,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST"))
        );
    }

    private String extractResourceId(JoinPoint joinPoint) {
        // 메서드 파라미터에서 리소스 ID 추출 로직
        return null;
    }
} 