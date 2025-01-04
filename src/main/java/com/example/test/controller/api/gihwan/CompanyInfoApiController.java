package com.example.test.controller.api.gihwan;

import com.example.test.dao.gihwan.CompanyInfoDao;
import com.example.test.dao.rim.CompanyDao;
import com.example.test.dto.CompanyDTO;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2025-01-05 by 최기환
 */
@RestController
@RequestMapping("/api/companies/info")
public class CompanyInfoApiController {

    private final CompanyInfoDao companyInfoDao;

    public CompanyInfoApiController(CompanyInfoDao companyInfoDao) {
        this.companyInfoDao = companyInfoDao;
    }

    @GetMapping
    public CompanyDTO getCompanyInfo(@RequestParam String companyId) {
        return companyInfoDao.findById(companyId);
    }

    @PutMapping
    public int updateCompanyInfo(@RequestBody CompanyDTO companyDTO) {
        return companyInfoDao.updateInfo(companyDTO);
    }
}
