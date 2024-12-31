package com.example.test.dao.sangin;

import com.example.test.dto.CompanyDTO;
import com.example.test.vo.JobPostDetailVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsersLikeDao_sangin {
    List<CompanyDTO> companyLike(String userId);
    List<JobPostDetailVo> jobPostLike(String userId);
}
