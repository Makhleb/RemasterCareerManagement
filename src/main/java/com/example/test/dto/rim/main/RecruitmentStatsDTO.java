package com.example.test.dto.rim.main;

import lombok.Data;

@Data
public class RecruitmentStatsDTO {
    private int activePostings;
    private int unreadResumes;
    private int totalApplications30Days;
    private int passCount30Days;
    private int failCount30Days;
    private int waitingCount30Days;
} 