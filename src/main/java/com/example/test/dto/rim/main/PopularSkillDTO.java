package com.example.test.dto.rim.main;

import lombok.Data;

@Data
public class PopularSkillDTO {
    private String skillCode;      // 기술 코드
    private String skillName;      // 기술 이름
    private int postCount;         // 해당 기술이 사용된 공고 수
    private int applicationCount;   // 해당 기술 공고의 총 지원자 수
} 