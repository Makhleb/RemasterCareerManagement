package com.example.test.dto;

import lombok.Data;
import java.util.Date;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private String userId; // 구직자 아이디
    private String userPw; // 패스워드
    private String userName; // 이름
    private String userPhone; // 휴대폰 번호
    private String userEmail; // 이메일
    private String userBirth; // 생년월일
    private String userGender; // 성별
    private LocalDateTime userCreateDate; // 가입일시
    private LocalDateTime userModifyDate; // 수정일시
    private String userRole; // 유저 역할
    private String userIsActive;
}
