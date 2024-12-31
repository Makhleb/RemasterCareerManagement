package com.example.test.service.sangin;

import com.example.test.dao.sangin.UsersLikeDao_sangin;
import com.example.test.dto.CompanyDTO;
import com.example.test.vo.JobPostDetailVo;
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

    public List<CompanyDTO> companyLike(String userId) {
        return usersLikeDao.companyLike(userId);
    }
    public List<JobPostDetailVo> jobPostLike(String userId){
        return usersLikeDao.jobPostLike(userId);
    }

}
