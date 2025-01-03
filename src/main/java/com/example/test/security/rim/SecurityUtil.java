package com.example.test.security.rim;

import com.example.test.exception.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

/**
 * Created on 2024-12-30 by 구경림
 * 인증된 사용자 정보를 쉽게 조회하기 위한 유틸리티 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityUtil {
    
    /**
     * 현재 인증된 사용자의 ID를 반환
     */
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null || 
            authentication.getPrincipal() instanceof String) {
            throw AuthException.unauthorized();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * 현재 인증된 사용자의 정보를 반환
     */
    public Map<String, Object> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 인증되지 않은 사용자
        if (authentication == null || authentication.getPrincipal() == null || 
            authentication.getPrincipal() instanceof String) {
            Map<String, Object> guestInfo = new HashMap<>();
            guestInfo.put("type", "guest");
            guestInfo.put("role", "ROLE_GUEST");
            return guestInfo;
        }

        // 인증된 사용자 정보 반환
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", userDetails.getUsername());
        userInfo.put("name", userDetails.getName());
        userInfo.put("role", userDetails.getRole());
        userInfo.put("type", userDetails.getRole().equals("ROLE_COMPANY") ? "company" : "user");
        
        return userInfo;
    }

    /**
     * 현재 인증된 사용자의 역할을 반환
     */
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw AuthException.unauthorized();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getRole();
    }

    /**
     * 특정 역할을 가지고 있는지 확인
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

    /**
     * 현재 사용자가 기업 회원인지 확인
     */
    public boolean isCompanyUser() {
        String role = getCurrentUserRole();
        return "ROLE_COMPANY".equals(role);
    }

    /**
     * 현재 사용자가 일반 회원인지 확인
     */
    public boolean isGeneralUser() {
        return hasRole("ROLE_USER");
    }

    /**
     * 현재 사용자가 관리자인지 확인
     */
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    /**
     * 리소스 소유자 확인
     * 관리자이거나 본인인 경우 true 반환
     */
    public boolean isResourceOwner(String resourceOwnerId) {
        if (isAdmin()) {
            return true;
        }
        String currentUserId = getCurrentUserId();
        return currentUserId.equals(resourceOwnerId);
    }

    public String getUserType() {
        Map<String, Object> userInfo = getCurrentUserInfo();
        return (String) userInfo.getOrDefault("type", "guest");
    }

    public boolean isValidUserType(String userType) {
        return userType != null && 
               (userType.equals("guest") || 
                userType.equals("user") || 
                userType.equals("company"));
    }
} 