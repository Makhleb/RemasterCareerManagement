package com.example.test.dto.rim;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResetPasswordDTO {
    private String userId;
    private String newPassword;
} 