package com.example.test.dto;

import lombok.Data;

@Data
public class ResumeDTO {
    private int resumeNo; // 이력서 인덱스
    private String title; // 제목
    private String addressDetail; // 상세주소
    private String address; // 주소
    private String zonecode; // 우편번호
    private String userId; // 구직자 아이디
    private String headshot; // 이력서 증명사진
    private String wishArea; // 희망근무지역
    private Integer wishSalary; // 희망 연봉
    private String wishTime; // 희망 근무 시간
    private String offerYn; // 기업 제안 수신
    private Date createDate; // 작성일
    private Date modifyDate; // 수정일
    private String deleteYn; // 삭제여부
    private String workCode; // 직무 코드
}
