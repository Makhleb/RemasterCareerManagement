package com.example.test.service.sangin;

import com.example.test.dao.sangin.JobPostDao_sangin;
import com.example.test.dto.*;
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

    public List<JobPostDetailVo> getJobPostAll(String userId) {
        return jobPostDao.getJobPostAll(userId);
    }

    public List<JobPostDetailVo> getJobPostMatching(String userId) {
        return jobPostDao.getJobPostMatching(userId);
    }

    public JobPostDetailVo getJobPost(String userId, int jobPostNo) {
        return jobPostDao.getJobPost(userId, jobPostNo);
    }

    public List<JobPostSkillDTO> getJobPostSkill(int jobPostNo) {
        return jobPostDao.getJobPostSkill(jobPostNo);
    }

    public List<BenefitDTO> getJobPostBenefit(int jobPostNo) {
        return jobPostDao.getJobPostBenefit(jobPostNo);
    }

    public List<ResumeDTO> getResumeList(String userId) {
        return jobPostDao.getResumeList(userId);
    }

    public int registAplcHstr(AplcHstrDTO aplcHstrDTO) {
        return jobPostDao.registAplcHstr(aplcHstrDTO);
    }

    ;
}
