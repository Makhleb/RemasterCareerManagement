package com.example.test.dto.request;

import lombok.Data;

/**
 * Created on 2025-01-05 by 최기환
 */
@Data
public class PasswordUpdateRequestDto {
    private String companyId;
    private String encodePw;
    private String beforePw;
    private String afterPw;
}
