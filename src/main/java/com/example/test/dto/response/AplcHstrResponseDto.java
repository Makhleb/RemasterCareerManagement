package com.example.test.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created on 2025-01-02 by 최기환
 */
@Data
public class AplcHstrResponseDto {
    private int jobPostNo;
    private int resumeNo;
    private String userName;
    private String title;
    private char passYn;
    private LocalDateTime aplcHstrDate;
}
