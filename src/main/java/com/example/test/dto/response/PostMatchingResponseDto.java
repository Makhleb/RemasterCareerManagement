package com.example.test.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created on 2025-01-04 by 최기환
 */
@Data
public class PostMatchingResponseDto {
    private int resumeNo;
    private String userName;
    private String title;
    private String userGender;
    private String userBirth;
    private int wishSalary;
    private LocalDateTime createDate;
    private String skillCodes;
    private int skillCnt;
}
