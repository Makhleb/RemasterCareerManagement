package com.example.test.dao;

import com.example.test.dto.CompanyDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyDao {
    CompanyDTO findById(String companyId);
} 