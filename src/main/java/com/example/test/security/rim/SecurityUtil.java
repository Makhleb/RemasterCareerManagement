package com.example.test.security.rim;

import com.example.test.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on 2024-12-30 by 구경림
 * 인증된 사용자 정보를 쉽게 조회하기 위한 유틸리티 클래스
 */
@Slf4j
public class SecurityUtil {
    
    /**
     * 현재 인증된 사용자의 ID를 반환
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("인증 정보가 없습니다");
        }
        return authentication.getName();
    }

    /**
     * 현재 인증된 사용자의 역할을 반환
     */
    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            throw new UnauthorizedException("인증 정보가 없습니다");
        }
        return authentication.getAuthorities().iterator().next().getAuthority();
    }

    /**
     * 특정 역할을 가지고 있는지 확인
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

    /**
     * 현재 사용자가 기업 회원인지 확인
     */
    public static boolean isCompanyUser() {
        String role = getCurrentUserRole();
        return "ROLE_COMPANY".equals(role);
    }

    /**
     * 현재 사용자가 일반 회원인지 확인
     */
    public static boolean isGeneralUser() {
        return hasRole("ROLE_USER");
    }

    /**
     * 현재 사용자가 관리자인지 확인
     */
    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    /**
     * 리소스 소유자 확인
     * 관리자이거나 본인인 경우 true 반환
     */
    public static boolean isResourceOwner(String resourceOwnerId) {
        if (isAdmin()) {
            return true;
        }
        String currentUserId = getCurrentUserId();
        return currentUserId.equals(resourceOwnerId);
    }
} 