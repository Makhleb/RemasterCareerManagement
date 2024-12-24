package com.example.test.dto;

import lombok.Data;

@Data
public class CommunityDTO {
    private int communityNo; // 게시판 번호
    private String userId; // 구직자 아이디
    private String title; // 제목
    private String content; // 내용
    private int viewCnt; // 조회수
    private Date createDate; // 등록일시
    private Date modifyDate; // 수정일시
}
