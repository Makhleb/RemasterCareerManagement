package com.example.test.dto;

import lombok.Data;

import java.util.List;

/**
 * Created on 2025-01-03 by 이경욱
 */
@Data
public class ResumeDetailDTO {

    private ResumeDTO resume;
    private ActivityDTO activities;
    private EducationDTO educations;
    private LicenseDTO licenses;
    private List<ResumeSkillDTO> skills;
    private MilitaryDTO military;
    private PotfolioDTO portfolios;
    private IntroduceDTO intro;

}
