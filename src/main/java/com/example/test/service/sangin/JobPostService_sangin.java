package com.example.test.service.sangin;

import com.example.test.dao.sangin.JobPostDao_sangin;
import com.example.test.dto.*;
import com.example.test.service.common.FileService;
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

    @Autowired
    private FileService fileService;

    public List<JobPostDetailVo> getJobPostAll(String userId, String keyword) {
        List<JobPostDetailVo> jobPostDetailVoList = jobPostDao.getJobPostAll(userId, keyword);
        int count = 0;
        for (JobPostDetailVo jobPostDetailVo : jobPostDetailVoList) {

            int jobPostNo = jobPostDetailVo.getJobPostNo();
            //파일 서비스를 위해서 String 타입으로 변환
            String jobPostNoStr = String.valueOf(jobPostNo);
            String companyId = jobPostDetailVo.getCompanyId();
            System.out.println("count = " + count++ + " / " + companyId);
            jobPostDetailVo.setPostThumbnail(fileService.loadImage("POST_THUMBNAIL", jobPostNoStr));
            jobPostDetailVo.setCompanyImage(fileService.loadImage("COMPANY_THUMBNAIL", companyId));
        }
        return jobPostDetailVoList;
    }

    public List<JobPostDetailVo> getJobPostMatching(String userId) {
        List<JobPostDetailVo> jobPostDetailVoList = jobPostDao.getJobPostMatching(userId);
        int count = 0;
        for (JobPostDetailVo jobPostDetailVo : jobPostDetailVoList) {

            int jobPostNo = jobPostDetailVo.getJobPostNo();
            String jobPostNoStr = String.valueOf(jobPostNo);
            String companyId = jobPostDetailVo.getCompanyId();
            System.out.println("count = " + count++ + " / " + companyId);

            jobPostDetailVo.setPostThumbnail(fileService.loadImage("POST_THUMBNAIL", jobPostNoStr));
            jobPostDetailVo.setCompanyImage(fileService.loadImage("COMPANY_THUMBNAIL", companyId));
        }
        return jobPostDetailVoList;
    }

    public JobPostDetailVo getJobPost(String userId, int jobPostNo) {
        JobPostDetailVo jobPost = jobPostDao.getJobPost(userId, jobPostNo);

        String jobPostNoStr = String.valueOf(jobPostNo);
        String companyId = jobPost.getCompanyId();

        jobPost.setPostThumbnail(fileService.loadImage("POST_THUMBNAIL", jobPostNoStr));
        jobPost.setCompanyImage(fileService.loadImage("COMPANY_THUMBNAIL", companyId));

        List<JobPostSkillDTO> skillList = jobPostDao.getJobPostSkill(jobPostNo);
        List<BenefitDTO> benefitList = jobPostDao.getJobPostBenefit(jobPostNo);
        jobPost.setSkillList(skillList);
        jobPost.setBenefitList(benefitList);
        return jobPost;
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

}
