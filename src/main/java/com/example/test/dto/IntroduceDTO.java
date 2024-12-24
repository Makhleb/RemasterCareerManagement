package com.example.test.dto;

import lombok.Data;

@Data
public class IntroduceDTO {
    private int resumeNo; // 이력서 인덱스
    private String title; // 제목
    private String content; // 내용
    private int order; // 순서
}
