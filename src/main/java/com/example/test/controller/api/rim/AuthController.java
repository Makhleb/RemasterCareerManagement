package com.example.test.controller.api.rim;

import com.example.test.dto.UserCreateDTO;
import com.example.test.dto.common.ApiResponse;
import com.example.test.exception.BadRequestException;
import com.example.test.service.AuthService;
import com.example.test.dto.CompanyCreateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

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
} 