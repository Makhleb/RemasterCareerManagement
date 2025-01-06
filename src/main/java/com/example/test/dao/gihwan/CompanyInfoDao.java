package com.example.test.dao.gihwan;

import com.example.test.dto.CompanyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created on 2025-01-05 by 최기환
 */
@Mapper
public interface CompanyInfoDao {
    CompanyDTO findById(@Param("companyId")String companyId);
    int updateInfo(@Param("C")CompanyDTO companyDTO);
    int updatePassword(@Param("companyId")String companyId, @Param("password") String password);
}
