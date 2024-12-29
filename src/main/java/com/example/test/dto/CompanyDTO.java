package com.example.test.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
public class CompanyDTO {
    private String companyId;            // 기업 아이디
    private String companyPw;            // 패스워드
    private String companyName;          // 회사명
    private String companyNumber;        // 사업자등록번호
    private String companyAddress;       // 회사주소
    private String companyZonecode;      // 회사우편번호
    private String companyAddressDetail; // 회사상세주소
    private String companyContact;       // 대표번호
    private String companyWebsite;       // 회사 홈페이지
    private String companyEmail;         // 이메일
    private LocalDate companyBirth;      // 창시일
    private Integer companyEmployee;     // 직원 수
    private LocalDateTime companyCreateDate;  // 가입일시
    private LocalDateTime companyModifyDate;  // 수정일시
    private String companyRole;          // 역할
    private String companyImage;         // 회사 대표 이미지
    private String companyIsActive;      // 활성화여부
    private Long companyProfit;          // 회사 연 수익(만원)
}
