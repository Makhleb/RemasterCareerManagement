package com.example.test.dto.wrapper;

import com.example.test.dto.BenefitDTO;
import com.example.test.dto.JobPostDTO;
import com.example.test.dto.JobPostSkillDTO;
import lombok.Data;

import java.util.List;

/**
 * Created on 2024-12-29 by 최기환
 */
@Data
public class JobPostWrapDto {
    private JobPostDTO jobPost;
    private List<BenefitDTO> benefits;
    private List<JobPostSkillDTO> jobPostSkills;
}
