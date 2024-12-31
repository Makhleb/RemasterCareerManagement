package com.example.test.service.sangin;

import com.example.test.dao.sangin.UsersLikeDao_sangin;
import com.example.test.dto.CompanyDTO;
import com.example.test.vo.JobPostDetailVo;
import com.example.test.vo.LikeCompanyVo;
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

    public List<LikeCompanyVo> companyLikeWithPosts(String userId) {
        // 얘는 유저아이디에 해당하는 기업 정보
        List<LikeCompanyVo> companies = usersLikeDao.companyLike(userId);
        // 얘는 전체 공고입니다~ // 컴퍼니 아이디로 조회.. ㄹㅇ 지렸다..
        for(LikeCompanyVo company : companies){
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


}
