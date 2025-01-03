package com.example.test.dto.rim.main;

import lombok.Data;

@Data
public class UserTypeDTO {
    private GuestSectionDTO guest;        // 비회원용 섹션
    private JobSeekerSectionDTO jobSeeker; // 일반회원용 섹션
    private CompanySectionDTO company;     // 기업회원용 섹션
}