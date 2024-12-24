package com.example.test.dto;

import lombok.Data;
import java.util.Date;

@Data
public class LikeDTO {
    private String userId; // 구직자 아이디
    private String companyId; // 기업 아이디
    private Date likeDate; // 관심 일시
    private String likeType; // 관심 구분 (구직자, 기업)
}
