package com.example.test.dao.gihwan;

import com.example.test.dto.response.AplcHstrResponseDto;
import com.example.test.dto.BenefitDTO;
import com.example.test.dto.JobPostDTO;
import com.example.test.dto.JobPostSkillDTO;
import com.example.test.dto.response.PostCompactResponseDto;
import com.example.test.dto.response.PostMatchingResponseDto;
import com.example.test.dto.wrapper.JobPostAplcWrapDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created on 2024-12-29 by 최기환
 */
@Mapper
public interface JobPostDao {
    void insertJobPost(@Param("JP")JobPostDTO jobPostDTO);
    int insertJobPostSkill(@Param("postNo") int postNo,@Param("list") List<JobPostSkillDTO> jobPostSkillDTOList);
    int insertBenefit(@Param("postNo") int postNo, @Param("list") List<BenefitDTO> benefitDTOList);

    List<JobPostAplcWrapDto> selectAll(@Param("companyId")String companyId, @Param("limit") Integer limit);
    List<AplcHstrResponseDto> selectJobPostAplc(@Param("jobPostNo") int jobPostNo, @Param("limit") Integer limit);
    List<PostCompactResponseDto> selectCompactPost(@Param("companyId") String companyId, @Param("limit") Integer limit);
    List<PostMatchingResponseDto> selectPostMatching(@Param("postNo") int postNo, @Param("limit") Integer limit);

    JobPostDTO selectJobPostDetail(@Param("jobPostNo") int jobPostNo);
    List<BenefitDTO> selectBenefit(@Param("jobPostNo") int jobPostNo);
    List<JobPostSkillDTO> selectPostSkill(@Param("jobPostNo") int jobPostNo);

    int updateJobPost(@Param("JP")JobPostDTO jobPostDTO);

    int deleteJobPost(@Param("jobPostNo") int jobPostNo);
    int deleteBenefit(@Param("jobPostNo") int jobPostNo);
    int deleteJobPostSkill(@Param("jobPostNo") int jobPostNo);
}
