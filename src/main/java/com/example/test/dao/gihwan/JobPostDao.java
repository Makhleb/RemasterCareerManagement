package com.example.test.dao.gihwan;

import com.example.test.dto.BenefitDTO;
import com.example.test.dto.JobPostDTO;
import com.example.test.dto.JobPostSkillDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created on 2024-12-29 by 최기환
 */
@Mapper
public interface JobPostDao {
    void insertJobPost(@Param("JP")JobPostDTO jobPostDTO);
    void insertJobPostSkill(@Param("postNo") int postNo,@Param("list") List<JobPostSkillDTO> jobPostSkillDTOList);
    void insertBenefit(@Param("postNo") int postNo, @Param("list") List<BenefitDTO> benefitDTOList);
}
