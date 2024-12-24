package com.example.test.dto;

import lombok.Data;

@Data
public class LicenseDTO {
    private int resumeNo; // 이력서 인덱스
    private String licenseName; // 자격증명
    private String licenseCenterName; // 발행처
    private Date passDate; // 합격년월
}
