package com.example.test.service.sangin;

import com.example.test.dao.sangin.UsersLikeDao_sangin;
import com.example.test.vo.CompanyDetailVo;
import com.example.test.vo.JobPostDetailVo;
import com.example.test.vo.LikeCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 2024-12-31 by 한상인
 */
@Service
public class UsersLikeService_sangin {

    @Autowired
    UsersLikeDao_sangin usersLikeDao;

    public List<CompanyDetailVo> companyLikeWithPosts(String userId) {
        // 얘는 유저아이디에 해당하는 기업 정보
        List<CompanyDetailVo> companies = usersLikeDao.companyLike(userId);
        // 얘는 전체 공고입니다~ // 컴퍼니 아이디로 조회.. 나 좀 지렸다..
        for(CompanyDetailVo company : companies){
            List<JobPostDetailVo> jobPosts = usersLikeDao.jobPostByCompanyId(company.getCompanyId());
            company.setJobPosts(jobPosts);
        }
        return companies;
    }

    public List<JobPostDetailVo> jobPostLike(String userId) {
        return usersLikeDao.jobPostLike(userId);
    }

    public LikeCountVo summary(String userId) {
        // 카운트 수
        int jplCount = usersLikeDao.jplCount(userId);
        int clCount = usersLikeDao.clCount(userId);
        // 객체 생성
        LikeCountVo likeCountVo = new LikeCountVo();
        likeCountVo.setJplCount(jplCount);
        likeCountVo.setClCount(clCount);
        // 라이크 vo 반환
        return likeCountVo;
    }
    //  u저 스크랩용입니다
    public int jplRemove(String userId, int jobPostNo) {
        return usersLikeDao.jplRemove(userId, jobPostNo);
    }
    public int jplAdd(String userId, int jobPostNo) {
        return usersLikeDao.jplAdd(userId, jobPostNo);
    }
    //  company 북마크용입니다
    public int clRemove(String userId, String companyId) {
        return usersLikeDao.clRemove(userId, companyId);
    }
    public int clAdd(String userId, String companyId) {
        return usersLikeDao.clAdd(userId, companyId);
    }


}
