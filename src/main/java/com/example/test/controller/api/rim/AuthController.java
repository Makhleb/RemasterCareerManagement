package com.example.test.controller.api.rim;

import com.example.test.dto.UserCreateDTO;
import com.example.test.exception.BadRequestException;
import com.example.test.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody UserCreateDTO dto) {
        if (!dto.getUserPw().equals(dto.getUserPwConfirm())) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다");
        }
        authService.signup(dto);
    }
} 