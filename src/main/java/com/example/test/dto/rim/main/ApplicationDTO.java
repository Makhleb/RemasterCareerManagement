package com.example.test.dto.rim.main;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApplicationDTO {
    private int jobPostNo;
    private String companyName;
    private String postTitle;
    private LocalDateTime applyDate;
    private String passYn;      // Y: 합격, N: 불합격, W: 대기
    private boolean readYn;     // 기업이 이력서를 읽었는지 여부
} 