package com.example.test.dto;

import lombok.Data;

@Data
public class CompanyScoreDTO {
    private String companyId; // 기업 아이디
    private String userId; // 구직자 아이디
    private int score; // 점수(1~5)
}
