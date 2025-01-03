package com.example.test.dto.rim.main;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CandidateDTO {
    private String userId;
    private String userName;
    private String headshot;
    private int careerYears;
    private List<String> skillCodes;
    private double matchingRate;    // 기술스택 매칭률
    private LocalDateTime lastActivityDate;  // 최근 구직활동 일시
} 