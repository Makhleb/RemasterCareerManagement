package com.example.test.dao.sangin;

import com.example.test.dto.*;
import com.example.test.vo.JobPostDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobPostDao_sangin {

    List<JobPostDetailVo> getJobPostAll(@Param("userId") String userId, @Param("keyword")String keyword);

    List<JobPostDetailVo> getJobPostMatching(@Param("userId") String userId);

    JobPostDetailVo getJobPost(@Param("userId")String userId,  @Param("jobPostNo") int jobPostNo);

    List<JobPostSkillDTO> getJobPostSkill(@Param("jobPostNo") int jobPostNo);

    List<BenefitDTO> getJobPostBenefit(@Param("jobPostNo") int jobPostNo);

    List<ResumeDTO> getResumeList(@Param("userId") String userId);

    int registAplcHstr(AplcHstrDTO aplcHstrDTO);
}
