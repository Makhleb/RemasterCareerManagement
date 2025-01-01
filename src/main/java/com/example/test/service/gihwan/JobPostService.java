package com.example.test.service.gihwan;

import com.example.test.dao.gihwan.JobPostDao;
import com.example.test.dto.JobPostDTO;
import com.example.test.dto.wrapper.JobPostWrapDto;
import com.example.test.service.common.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 2024-12-29 by 최기환
 */
@Service
public class JobPostService {

    private final JobPostDao jobPostDao;
    private final FileService fileService;

    public JobPostService(JobPostDao jobPostDao, FileService fileService) {
        this.jobPostDao = jobPostDao;
        this.fileService = fileService;
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


    public List<JobPostDTO> selectAllJobPost(String companyId) {
        List<JobPostDTO> daoResult = jobPostDao.selectAll(companyId);
        for (JobPostDTO listItem : daoResult) {
            String fileValue = fileService.loadImage("POST_THUMBNAIL", String.valueOf(listItem.getJobPostNo()));
            listItem.setPostThumbnail(fileValue);
        }
        return daoResult;
    }
}
