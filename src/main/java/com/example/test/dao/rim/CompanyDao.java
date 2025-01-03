package com.example.test.dao.rim;

import com.example.test.dto.CompanyDTO;
import com.example.test.dto.rim.CompanyCreateDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;

@Mapper
public interface CompanyDao {
    Optional<CompanyDTO> findById(String companyId);
    void save(CompanyCreateDTO dto);
    boolean existsByEmail(String email);
    boolean existsById(String companyId);
    boolean existsByNumber(String number);
} 