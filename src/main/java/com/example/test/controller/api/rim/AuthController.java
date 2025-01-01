package com.example.test.controller.api.rim;

import com.example.test.dto.rim.LoginDTO;
import com.example.test.dto.rim.UserCreateDTO;
import com.example.test.dto.common.ApiResponse;
import com.example.test.exception.AuthException;
import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.rim.AuthService;
import com.example.test.dto.rim.CompanyCreateDTO;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.test.exception.BusinessException;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final SecurityUtil securityUtil;

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody UserCreateDTO dto) {
        if (!dto.getUserPw().equals(dto.getUserPwConfirm())) {
            throw BusinessException.badRequest("비밀번호가 일치하지 않습니다");
        }
        authService.signup(dto);
        return "회원가입이 완료되었습니다";
    }

    @PostMapping("/check-duplicate")
    public Map<String, Boolean> checkDuplicate(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        if (userId == null || userId.trim().isEmpty()) {
            throw BusinessException.badRequest("아이디는 필수입니다");
        }
        
        boolean isAvailable = !authService.checkUserIdExists(userId);
        return Map.of("success", isAvailable);
    }

    @PostMapping("/company/signup")
    public String companySignup(@Valid @RequestBody CompanyCreateDTO dto) {
        authService.companySignup(dto);
        return "회원가입이 완료되었습니다";
    }

    @PostMapping("/company/check-duplicate")
    public Map<String, Boolean> checkCompanyDuplicate(@RequestBody Map<String, String> request) {
        String companyId = request.get("companyId");
        if (companyId == null || companyId.trim().isEmpty()) {
            throw BusinessException.badRequest("아이디는 필수입니다");
        }
        
        boolean isAvailable = !authService.checkCompanyIdExists(companyId);
        return Map.of("success", isAvailable);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginDTO dto, HttpServletResponse response) {
        // 1. 로그인 검증 및 JWT 토큰 생성
        String token = authService.login(dto);
        log.info("Generated JWT token: {}", token);
        
        // 2. HttpOnly 쿠키에 JWT 저장
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);  // XSS 방지
        cookie.setPath("/");
        // cookie.setMaxAge(3600);
        
        response.addCookie(cookie);
        log.info("Set JWT cookie for user: {}", dto.getUserId());
    }

    @PostMapping("/company/login")
    public void companyLogin(@RequestBody LoginDTO dto, HttpServletResponse response) {
        String token = authService.companyLogin(dto);
        log.info("Generated JWT token for company: {}", token);
        
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // cookie.setMaxAge(3600);
        
        response.addCookie(cookie);
        log.info("Set JWT cookie for company: {}", dto.getUserId());
    }

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser() {
        log.info("getCurrentUser 호출됨");
        Map<String, Object> userInfo = securityUtil.getCurrentUserInfo();
        log.info("현재 사용자 정보: {}", userInfo);
        return userInfo;
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        // 쿠키 삭제
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
    }
} 