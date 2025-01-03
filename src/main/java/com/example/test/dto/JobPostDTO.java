package com.example.test.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class JobPostDTO {
    private int jobPostNo; // 공고 인덱스
    private String companyId; // 기업 아이디
    private String title; // 공고 제목
    private Integer jobHistory; // 경력
    private Integer jobSalary; // 급여
    private LocalDateTime  startDate; // 공고 시작일
    private LocalDateTime  endDate; // 공고 마감일
    private String content; // 모집부분 및 상세내용
    private String method; // 접수기간 및 방법
    private String addNotice; // 추가 유의사항
    private String managerName; // 담당자명
    private String managerPhone; // 담당자 전화번호
    private String managerEmail; // 담당자 이메일
    private String postViewYn; // 삭제여부
    private String postThumbnail; // 대표 이미지 썸네일
    private String postAddress; // 근무지주소
    private String postAddressDetail; // 근무지상세주소
    private String postZonecode; // 근무지우편번호
    private String workCode; // 직무 코드
    private String jobRankCode; // 직급/직책 코드
    private String workTypeCode; // 근무형태 코드
    private String educationCode; // 학력 코드
}
