package com.example.test.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CompanyDTO {
    private String companyId;
    private String companyPw;
    private String companyName;
    private String companyNumber;
    private String companyAddress;
    private String companyZonecode;
    private String companyAddressDetail;
    private String companyContact;
    private String companyWebsite;
    private String companyEmail;
    private String companyBirth;
    private Integer companyEmployee;
    private Long companyProfit;
    private LocalDateTime companyCreateDate;
    private LocalDateTime companyModifyDate;
    private String companyRole;
    private String companyIsActive;
    private String companyImage;
    private Double avgRating;
    private Integer reviewCount;
    private Integer likeCount;
    private Integer activeJobCount;
}
