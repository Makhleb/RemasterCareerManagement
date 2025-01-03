package com.example.test.dto.rim.main;

import lombok.Data;
import java.util.List;

@Data
public class DashboardDTO {
    private ApplicationStatsDTO stats;
    private List<ApplicationDTO> recentApplications;
    
    @Data
    public static class ApplicationStatsDTO {
        private int totalCount;      // 총 지원 건수
        private int passCount;       // 합격 건수
        private int failCount;       // 불합격 건수
        private int waitingCount;    // 대기중인 건수
        private double passRate;     // 합격률
    }
} 