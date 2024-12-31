package com.example.test.service.gihwan;

import com.example.test.dao.gihwan.JobPostDao;
import com.example.test.dto.wrapper.JobPostWrapDto;
import org.springframework.stereotype.Service;

/**
 * Created on 2024-12-29 by 최기환
 */
@Service
public class JobPostService {

    private final JobPostDao jobPostDao;

    public JobPostService(JobPostDao jobPostDao) {
        this.jobPostDao = jobPostDao;
    }

    public int postJobPost(JobPostWrapDto jobPostWrapDto) {
        int insertedNo = 0;

        jobPostDao.insertJobPost(jobPostWrapDto.getJobPost());
        insertedNo = jobPostWrapDto.getJobPost().getJobPostNo();

        if(!jobPostWrapDto.getJobPostSkills().isEmpty()){
            jobPostDao.insertJobPostSkill(insertedNo, jobPostWrapDto.getJobPostSkills());
        }
        if(!jobPostWrapDto.getBenefits().isEmpty()){
            jobPostDao.insertBenefit(insertedNo, jobPostWrapDto.getBenefits());
        }
        return insertedNo;
    }
}
