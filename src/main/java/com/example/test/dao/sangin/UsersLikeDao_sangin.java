package com.example.test.dao.sangin;

import com.example.test.vo.CompanyDetailVo;
import com.example.test.vo.JobPostDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersLikeDao_sangin {
    List<JobPostDetailVo> jobPostLike(String userId);
    int jplCount(String userId);
    int clCount(String userId);
    //1, 2 얘네 둘이 같이 사용함 // 조인으로 못하겠음~~
    //1
    List<CompanyDetailVo> companyLike(String userId);
    //2 얘는 아이디 없어두 됨 그냥 랜덤으로 묶어주면 됨 // 와 컴퍼니 아이디는 ㄹㅇ 지렸다.....
    List<JobPostDetailVo> jobPostByCompanyId(String companyId);
//jpl용
    int jplRemove(@Param("userId") String userId, @Param("jobPostNo") int jobPostNo);
    int jplAdd(@Param("userId") String userId, @Param("jobPostNo") int jobPostNo);
//cl용
    int clRemove(@Param("userId") String userId, @Param("companyId") String companyId);
    int clAdd(@Param("userId") String userId, @Param("companyId") String companyId);
}
