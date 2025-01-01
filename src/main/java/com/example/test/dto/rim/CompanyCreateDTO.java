package com.example.test.dto.rim;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CompanyCreateDTO {
    @NotBlank(message = "아이디는 필수입니다")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "아이디는 영문, 숫자 4~20자여야 합니다")
    private String companyId;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", 
            message = "비밀번호는 영문, 숫자, 특수문자 조합 8자 이상이어야 합니다")
    private String companyPw;

    @NotBlank(message = "비밀번호 확인은 필수입니다")
    private String companyPwConfirm;

    @NotBlank(message = "회사명은 필수입니다")
    private String companyName;

    @NotBlank(message = "사업자등록번호는 필수입니다")
    @Pattern(regexp = "^\\d{10}$", message = "올바른 사업자등록번호 형식이 아닙니다")
    private String companyNumber;

    @NotBlank(message = "회사 주소는 필수입니다")
    private String companyAddress;

    @NotBlank(message = "우편번호는 필수입니다")
    private String companyZonecode;

    @NotBlank(message = "상세주소는 필수입니다")
    private String companyAddressDetail;

    @NotBlank(message = "대표번호는 필수입니다")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호 형식이 아닙니다")
    private String companyContact;

    @NotBlank(message = "이메일은 필수입니다")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "올바른 이메일 형식이 아닙니다")
    private String companyEmail;

    // 선택 필드들
    private String companyWebsite;
    private Date companyBirth;
    private Integer companyEmployee;
    private Long companyProfit;
} 