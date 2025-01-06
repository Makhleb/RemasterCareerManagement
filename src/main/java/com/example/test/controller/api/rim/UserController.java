package com.example.test.controller.api.rim;

import com.example.test.dto.UserDTO;
import com.example.test.dto.common.ApiResponse;
import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.rim.AuthService;
import com.example.test.dto.rim.UserUpdateDTO;
import com.example.test.dto.rim.ResetPasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.test.security.rim.RequireToken;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final SecurityUtil securityUtil;

    @GetMapping("/me")
    @RequireToken(roles = {"ROLE_USER", "ROLE_COMPANY"})
    public ApiResponse<UserDTO> getMyInfo() {
        String userId = securityUtil.getCurrentUserId();
        return ApiResponse.success(authService.getUserInfo(userId));
    }

    @PutMapping("/me")
    @RequireToken(roles = {"ROLE_USER"})
    public ApiResponse<Void> updateMyInfo(@RequestBody UserUpdateDTO dto) {
        String userId = securityUtil.getCurrentUserId();
        authService.updateUser(userId, dto);
        return ApiResponse.success(null, "회원정보가 수정되었습니다.");
    }

} 