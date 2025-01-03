package com.example.test.dto.rim.main;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private int score;
    private String content;
    private LocalDateTime reviewDate;
    private String userId;  // 익명처리된 ID
} 