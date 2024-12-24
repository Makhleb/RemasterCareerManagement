package com.example.test.dto;

import lombok.Data;
import java.util.Date;

@Data
public class CommentDTO {
    private int commentNo; // 댓글 번호
    private int communityNo; // 게시판 번호
    private String userId; // 구직자 아이디
    private String commentContent; // 내용
    private Date commentCreateDate; // 등록일시
    private Date commentModifyDate; // 수정일시
}
