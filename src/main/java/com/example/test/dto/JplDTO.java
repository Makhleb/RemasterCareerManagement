package com.example.test.dto;

import lombok.Data;
import java.util.Date;

@Data
public class JplDTO {
    private int jobPostNo; // 공고 인덱스
    private String userId; // 구직자 아이디
    private Date scrapDate; // 스크랩 일시
}
