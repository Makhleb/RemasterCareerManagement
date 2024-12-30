package com.example.test.dao.sangin;

import com.example.test.dto.JobPostDTO;
import com.example.test.dto.JobPostSkillDTO;
import com.example.test.vo.JobPostDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobPostDao_sangin {

    List<JobPostDTO> getJobPostAll();
    List<JobPostDTO> getJobPostMatching();

    JobPostDetailVo getJobPost(@Param("jobPostNo") int jobPostNo);
    List<JobPostSkillDTO> getJobPostSkill(@Param("jobPostNo") int jobPostNo);

}
