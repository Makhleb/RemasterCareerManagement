package com.example.test.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UserCreateDTO {
    @NotBlank(message = "아이디는 필수입니다")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "아이디는 영문과 숫자 4~20자리여야 합니다")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", 
            message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다")
    private String userPw;

    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String userPwConfirm;

    @NotBlank(message = "이름은 필수입니다")
    private String userName;

    @NotBlank(message = "휴대폰 번호는 필수입니다")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "올바른 휴대폰 번호 형식이 아닙니다")
    private String userPhone;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String userEmail;

    @NotBlank(message = "생년월일은 필수입니다")
    private String userBirth;

    @NotBlank(message = "성별은 필수입니다")
    private String userGender;
} 