package com.example.test.dto.rim;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FindPasswordRequest {
    @NotBlank(message = "아이디를 입력해주세요")
    private String userId;
    
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String userEmail;
    
    @NotBlank(message = "전화번호를 입력해주세요")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다")
    private String userPhone;
} 