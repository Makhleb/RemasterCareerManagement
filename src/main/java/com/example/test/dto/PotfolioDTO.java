package com.example.test.dto;

import lombok.Data;

@Data
public class PotfolioDTO {
    private int resumeNo; // 이력서 인덱스
    private String potfolioFilename; // 포폴 제목
    private String gubn; // 파일 OR 링크
}
