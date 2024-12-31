package com.example.test.dao.sangin;

import com.example.test.dto.CompanyDTO;
import com.example.test.vo.JobPostDetailVo;
import com.example.test.vo.LikeCompanyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsersLikeDao_sangin {
    List<JobPostDetailVo> jobPostLike(String userId);
    int jplCount(String userId);
    int clCount(String userId);
    //1, 2 얘네 둘이 같이 사용함 // 조인으로 못하겠음~~
    //1
    List<LikeCompanyVo> companyLike(String userId);
    //2 얘는 아이디 없어두 됨 그냥 랜덤으로 묶어주면 됨 // 와 컴퍼니 아이디는 ㄹㅇ 지렸다.....
    List<JobPostDetailVo> jobPostByCompanyId(String companyId);
}
