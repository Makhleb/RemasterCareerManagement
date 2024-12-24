package com.example.test.dto;

import lombok.Data;
import java.util.Date;

@Data
public class EducationDTO {
    private int resumeNo; // 이력서 인덱스
    private String educationCode; // 학력 코드
    private String schoolName; // 학교명
    private String specialty; // 전공
    private Date enterDate; // 입학년월
    private Date graduateDate; // 졸업연월
}
