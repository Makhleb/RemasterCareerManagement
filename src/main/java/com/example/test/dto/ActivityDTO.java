package com.example.test.dto;

import lombok.Data;

@Data
public class ActivityDTO {
    private int resumeNo; // 이력서 인덱스
    private String activityType; // 활동구분
    private int activityOrder; // 활동 순서
    private Date startDate; // 시작년월
    private Date endDate; // 종료년월
    private String activityCenterName; // 활동기관명
    private String activityContent; // 주요활동 내용
    private Boolean isFinished;
}
