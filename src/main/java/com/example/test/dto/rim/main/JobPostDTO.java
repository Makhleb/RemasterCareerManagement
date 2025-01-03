package com.example.test.dto.rim.main;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
public class JobPostDTO {
    private int jobPostNo;
    private String companyId;
    private String companyName;
    private String companyImage;
    private String title;
    private int jobHistory;
    private Integer jobSalary;
    private LocalDateTime endDate;
    private String content;
    private String method;
    private String addNotice;
    private String postViewYn;
    private String postThumbnail;
    private String workCode;
    private List<String> skillCodes;
    private List<String> benefits;
    private int scrapCount;
    private int applicationCount;
    private LocalDateTime startDate;
    private int matchingSkillCount;
    private long dday;
    
    public JobPostDTO() {
        calculateDday();
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        calculateDday();
    }
    
    private void calculateDday() {
        if (this.endDate == null) {
            this.dday = 0;
            return;
        }
        
        LocalDateTime now = LocalDateTime.now();
        long remainingDays = ChronoUnit.DAYS.between(now.toLocalDate(), this.endDate.toLocalDate());
        
        // 마감 시간이 지났는지 확인
        this.dday = now.isAfter(this.endDate) ? 1 : -remainingDays;
    }
} 