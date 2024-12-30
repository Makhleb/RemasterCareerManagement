package com.example.test.dao;

import com.example.test.dto.CompanyDTO;
import com.example.test.dto.CompanyCreateDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyDao {
    CompanyDTO findById(String companyId);
    void save(CompanyCreateDTO dto);
    boolean existsByEmail(String email);
    boolean existsById(String companyId);
    boolean existsByNumber(String number);
} 