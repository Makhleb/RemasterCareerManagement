package com.example.test.dao.sangin;

import com.example.test.dto.CompanyScoreDTO;
import com.example.test.vo.CompanyDetailVo;
import com.example.test.vo.JobPostDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyDetailDao_sangin {
    public CompanyDetailVo getCompanyDetailById(@Param("companyId") String companyId);

    public List<JobPostDetailVo> jobPostByCompanyId(@Param("companyId")String companyId);

    public List<CompanyScoreDTO> companyScoreByCompanyId(@Param("companyId")String companyId);
}
