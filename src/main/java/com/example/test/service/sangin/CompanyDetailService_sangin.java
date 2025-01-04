package com.example.test.service.sangin;

import com.example.test.dao.sangin.CompanyDetailDao_sangin;
import com.example.test.dto.CompanyScoreDTO;
import com.example.test.vo.CompanyDetailVo;
import com.example.test.vo.JobPostDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2025-01-03 by 한상인
 */
@Service
public class CompanyDetailService_sangin {
    @Autowired
    CompanyDetailDao_sangin companyDetailDao;

    public CompanyDetailVo getCompanyDetailById(String companyId, String userId) {
        System.out.println("company detail service1..." + companyId);
        CompanyDetailVo companyDetail = companyDetailDao.getCompanyDetailById(companyId, userId);
//        companyId 에 해당하는 회사 정보 찾아오기
        System.out.println("company detail service2..." + companyDetail);
//        companyId 에 해당하는 공고랑 score 찾기
        List<JobPostDetailVo> jobPosts = companyDetailDao.jobPostByCompanyId(companyId);
        List<CompanyScoreDTO> scores = companyDetailDao.companyScoreByCompanyId(companyId);
        System.out.println("company detail service3..." + companyDetail);
//        배열 넣어주기
        companyDetail.setJobPosts(jobPosts);
        companyDetail.setScores(scores);

        return companyDetail;
    }

}
