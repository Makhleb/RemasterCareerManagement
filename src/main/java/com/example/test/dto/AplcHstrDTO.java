package com.example.test.dto;

import lombok.Data;

@Data
public class AplcHstrDTO {
    private int jobPostNo; // 공고 인덱스
    private int resumeNo; // 이력서 인덱스
    private Date aplcHstrDate; // 지원일시
    private String passYn; // 합/불/대기 여부
}
