package com.example.test.service.gihwan;

import com.example.test.dao.gihwan.JobPostDao;
import com.example.test.dto.JobPostDTO;
import com.example.test.dto.response.PostCompactResponseDto;
import com.example.test.dto.response.PostMatchingResponseDto;
import com.example.test.dto.wrapper.JobPostAplcWrapDto;
import com.example.test.dto.wrapper.JobPostWrapDto;
import com.example.test.service.common.FileService;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

        if (!jobPostWrapDto.getJobPostSkills().isEmpty()) {
            jobPostDao.insertJobPostSkill(insertedNo, jobPostWrapDto.getJobPostSkills());
        }
        if (!jobPostWrapDto.getBenefits().isEmpty()) {
            jobPostDao.insertBenefit(insertedNo, jobPostWrapDto.getBenefits());
        }
        return insertedNo;
    }

    public List<JobPostAplcWrapDto> selectAllJobPost(String companyId, Integer limit) {
        List<JobPostAplcWrapDto> daoResult = jobPostDao.selectAll(companyId, limit);
        for (JobPostAplcWrapDto listItem : daoResult) {
            listItem.setAlpcList(jobPostDao.selectJobPostAplc(listItem.getJobPostNo(), limit));
            String fileValue = fileService.loadImage("POST_THUMBNAIL", String.valueOf(listItem.getJobPostNo()));
            listItem.setPostThumbnail(fileValue);
        }
        return daoResult;
    }

    public JobPostWrapDto selectDetail(int detailNo) {
        JobPostWrapDto wrapDto = new JobPostWrapDto();
        String fileValue = fileService.loadImage("POST_THUMBNAIL", String.valueOf(detailNo));

        wrapDto.setJobPost(jobPostDao.selectJobPostDetail(detailNo));
        wrapDto.setBenefits(jobPostDao.selectBenefit(detailNo));
        wrapDto.setJobPostSkills(jobPostDao.selectPostSkill(detailNo));
        wrapDto.getJobPost().setPostThumbnail(fileValue);
        return wrapDto;
    }

    public boolean updatePost(JobPostWrapDto jobPostWrapDto){
        int jobPostNo = jobPostWrapDto.getJobPost().getJobPostNo();
        int result = jobPostDao.updateJobPost(jobPostWrapDto.getJobPost());
        jobPostDao.deleteBenefit(jobPostNo);
        jobPostDao.deleteJobPostSkill(jobPostNo);
        jobPostDao.insertBenefit(jobPostNo, jobPostWrapDto.getBenefits());
        jobPostDao.insertJobPostSkill(jobPostNo, jobPostWrapDto.getJobPostSkills());

        return result != 0;
    }

    public boolean deleteJobPost(int jobPostNo) {
        return jobPostDao.deleteJobPost(jobPostNo) != 0;
    }

    public List<PostCompactResponseDto> getCompactList(String companyId) {
        return jobPostDao.selectCompactPost(companyId);
    }

    public List<PostMatchingResponseDto> getMatchingList(int postNo) {
        return jobPostDao.selectPostMatching(postNo);
    }
}
