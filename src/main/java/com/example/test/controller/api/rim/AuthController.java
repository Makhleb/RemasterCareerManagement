package com.example.test.controller.api.rim;

import com.example.test.dto.rim.LoginDTO;
import com.example.test.dto.rim.UserCreateDTO;
import com.example.test.dto.common.ApiResponse;
import com.example.test.exception.AuthException;
import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.rim.AuthService;
import com.example.test.dto.rim.CompanyCreateDTO;
import com.example.test.util.rim.JwtUtil;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.test.exception.BusinessException;
import java.util.Map;
import com.example.test.dto.rim.FindPasswordRequest;
import com.example.test.dto.rim.ResetPasswordRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final SecurityUtil securityUtil;
    private final JwtUtil jwtUtil;

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
    public Map<String, String> login(@RequestBody LoginDTO dto, HttpServletResponse response) {
        String token = authService.login(dto);
        return authService.handleLoginSuccess(token, dto.getUserId(), response);
    }

    @PostMapping("/company/login")
    public Map<String, String> companyLogin(@RequestBody LoginDTO dto, HttpServletResponse response) {
        String token = authService.companyLogin(dto);
        return authService.handleLoginSuccess(token, dto.getUserId(), response);
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
        authService.logout(response);  // 서비스로 위임
        SecurityContextHolder.clearContext();  // 시큐리티 컨텍스트 클리어
    }

    @PostMapping("/find-password")
    public ApiResponse<Map<String, String>> findPassword(@Valid @RequestBody FindPasswordRequest request) {
        String userId = authService.findUserByIdEmailAndPhone(
            request.getUserId(),
            request.getUserEmail(), 
            request.getUserPhone()
        );
        
        if (userId != null) {
            return ApiResponse.success(Map.of("userId", userId));
        } else {
            throw AuthException.authenticationFailed("일치하는 사용자 정보를 찾을 수 없습니다");
        }
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getUserId(), request.getNewPassword());
        return ApiResponse.success(null, "비밀번호가 성공적으로 변경되었습니다");
    }
} 