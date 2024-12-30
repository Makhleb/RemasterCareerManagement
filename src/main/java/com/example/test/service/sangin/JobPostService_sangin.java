package com.example.test.service.sangin;

import com.example.test.dao.sangin.JobPostDao_sangin;
import com.example.test.dto.JobPostDTO;
import com.example.test.dto.JobPostSkillDTO;
import com.example.test.vo.JobPostDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 2024-12-30 by 한상인
 */
@Service
public class JobPostService_sangin {
    @Autowired
    JobPostDao_sangin jobPostDao;

    public List<JobPostDTO> getJobPostAll(){
        return jobPostDao.getJobPostAll();
    }
    public List<JobPostDTO> getJobPostMatching(){

        return jobPostDao.getJobPostMatching();
    }

    public JobPostDetailVo getJobPost(int jobPostNo){
        return jobPostDao.getJobPost(jobPostNo);
    }
    public List<JobPostSkillDTO> getJobPostSkill(int jobPostNo){
        return jobPostDao.getJobPostSkill(jobPostNo);
    }
}
