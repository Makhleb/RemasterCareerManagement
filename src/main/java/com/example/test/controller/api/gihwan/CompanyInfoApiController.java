package com.example.test.controller.api.gihwan;

import com.example.test.dao.gihwan.CompanyInfoDao;
import com.example.test.dao.rim.CompanyDao;
import com.example.test.dto.CompanyDTO;
import com.example.test.dto.request.PasswordUpdateRequestDto;
import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.common.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2025-01-05 by 최기환
 */
@RestController
@RequestMapping("/api/companies/info")
public class CompanyInfoApiController {

    private final CompanyInfoDao companyInfoDao;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    public CompanyInfoApiController(CompanyInfoDao companyInfoDao, FileService fileService, PasswordEncoder passwordEncoder) {
        this.companyInfoDao = companyInfoDao;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public CompanyDTO getCompanyInfo(@RequestParam String companyId) {
        CompanyDTO companyDTO = companyInfoDao.findById(companyId);
        companyDTO.setCompanyImage(fileService.loadImage("COMPANY_THUMBNAIL",companyId));
        return companyDTO;
    }

    @PutMapping
    public int updateCompanyInfo(@RequestBody CompanyDTO companyDTO) {
        return companyInfoDao.updateInfo(companyDTO);
    }

    @PutMapping("/password")
    public int updatePasswordInfo(@RequestBody PasswordUpdateRequestDto request) {
        String encodeAfterPw = passwordEncoder.encode(request.getAfterPw());
        if(!passwordEncoder.matches(request.getBeforePw(), request.getEncodePw())){
            return -1;
        }

        return companyInfoDao.updatePassword(request.getCompanyId(), encodeAfterPw);
    }
}
