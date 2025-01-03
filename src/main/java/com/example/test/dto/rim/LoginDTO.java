package com.example.test.dto.rim;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 2024-12-31 by 구경림
 */
@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "아이디는 필수입니다")
    private String userId;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String userPw;
}