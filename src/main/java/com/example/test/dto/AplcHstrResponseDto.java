package com.example.test.dto;

import lombok.Data;

import java.util.Date;

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
    private Date aplcHstrDate;
}
