package com.example.test.controller.api.rim;

import com.example.test.dto.rim.LoginDTO;
import com.example.test.dto.rim.UserCreateDTO;
import com.example.test.dto.common.ApiResponse;
import com.example.test.exception.BadRequestException;
import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.rim.AuthService;
import com.example.test.dto.rim.CompanyCreateDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.test.security.rim.RequireToken;
import com.example.test.dto.UserDTO;
import com.example.test.dto.CompanyDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.test.exception.UnauthorizedException;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody UserCreateDTO dto) {
        // @Valid 어노테이션으로 DTO의 유효성 검사
        if (!dto.getUserPw().equals(dto.getUserPwConfirm())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다");
        }
        authService.signup(dto);
        return "회원가입이 완료되었습니다";
    }

    @PostMapping("/check-duplicate")
    public Map<String, Boolean> checkDuplicate(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        System.out.println(userId);
        if (userId == null || userId.trim().isEmpty()) {
            throw new BadRequestException("아이디는 필수입니다");
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
            throw new BadRequestException("아이디는 필수입니다");
        }
        
        boolean isAvailable = !authService.checkCompanyIdExists(companyId);
        return Map.of("success", isAvailable);
    }

    @PostMapping("/login")
    public ApiResponse<Void> login(@RequestBody LoginDTO dto, HttpServletResponse response) {
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
        return ApiResponse.success(null);
    }

    @PostMapping("/company/login")
    public ApiResponse<Void> companyLogin(@RequestBody LoginDTO dto, HttpServletResponse response) {
        String token = authService.companyLogin(dto);
        log.info("Generated JWT token for company: {}", token);
        
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        
        response.addCookie(cookie);
        log.info("Set JWT cookie for company: {}", dto.getUserId());
        return ApiResponse.success(null);
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> getCurrentUser() {
        // 인증되지 않은 경우 401 에러 발생
        String userId = SecurityUtil.getCurrentUserId(); // UnauthorizedException 발생
        String role = SecurityUtil.getCurrentUserRole();
        
        Map<String, Object> userInfo = new HashMap<>();
        
        if (SecurityUtil.isCompanyUser()) {
            CompanyDTO company = authService.getCompanyInfo(userId);
            userInfo.put("id", company.getCompanyId());
            userInfo.put("name", company.getCompanyName());
            userInfo.put("type", "company");
        } else {
            UserDTO user = authService.getUserInfo(userId);
            userInfo.put("id", user.getUserId());
            userInfo.put("name", user.getUserName());
            userInfo.put("type", "user");
        }
        userInfo.put("role", role);
        
        return ApiResponse.success(userInfo);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
        return ApiResponse.success(null);
    }
} 