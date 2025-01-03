package com.example.test.service.rim;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.test.dao.rim.CompanyDao;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyService {
    
    private final CompanyDao companyDao;

    // TOP 10 기업 조회 (별점 기준)
    public List<Map<String, Object>> findTopCompaniesByRating() {
        return companyDao.findTopCompaniesByRating();
    }
} 