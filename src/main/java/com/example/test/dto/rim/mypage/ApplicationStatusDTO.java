package com.example.test.dto.rim.mypage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ApplicationStatusDTO {
    private ApplicationStats stats;
    private List<ApplicationDetail> applications;

    @Getter
    @Setter
    @Builder
    public static class ApplicationStats {
        private int total;
        private int inProgress;
        private int pass;
        private int fail;
    }

    @Getter
    @Setter
    @Builder
    public static class ApplicationDetail {
        private Long applicationId;
        private String companyId;
        private String companyName;
        private String jobTitle;
        private String status;  // W: 대기, Y: 합격, N: 불합격
        private LocalDateTime appliedDate;
        private Boolean hasScore;  // 평점 존재 여부
        private Integer score;  // 추가
    }
} 