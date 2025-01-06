package com.example.test.security.rim;

import com.example.test.exception.AuthException;
import com.example.test.service.gyeonguk.ResumeService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
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
    private final ResumeService resumeService;

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
        // 3. checkResume=true인 경우 이력서 접근 권한 체크
        if (requireToken.checkResume()) {
            int resumeNo = extractResumeNo(joinPoint);
            String userId = securityUtil.getCurrentUserId();
            
            if (securityUtil.hasRole("ROLE_USER")) {
                // 일반 사용자: 자신의 이력서만 조회 가능
                if (!resumeService.isOwner(resumeNo, userId)) {
                    throw AuthException.forbidden("자신의 이력서만 조회할 수 있습니다");
                }
            } 
            else if (securityUtil.hasRole("ROLE_COMPANY")) {
                // 기업 회원: 지원받은 이력서만 조회 가능
                if (!resumeService.isAppliedToCompany(resumeNo, userId)) {
                    throw AuthException.forbidden("지원받은 이력서만 조회할 수 있습니다");
                }
            }
            // ROLE_ADMIN은 모든 이력서 조회 가능
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

    private int extractResumeNo(JoinPoint joinPoint) {
        try {
            // PathVariable에서 resumeNo 추출
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            
            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i].equals("resumeNo")) {
                    return (int) args[i];
                }
            }
            throw new IllegalArgumentException("resumeNo 파라미터를 찾을 수 없습니다");
        } catch (Exception e) {
            log.error("이력서 번호 추출 중 오류 발생", e);
            throw AuthException.forbidden("이력서 접근 권한이 없습니다");
        }
    }
} 