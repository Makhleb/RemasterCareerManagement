package com.example.test.controller.api.sangin;

import com.example.test.dto.CompanyScoreDTO;
import com.example.test.service.sangin.CompanyDetailService_sangin;
import com.example.test.vo.CompanyDetailVo;
import com.example.test.vo.JobPostDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2025-01-03 by 한상인
 */
@RestController
@RequestMapping("/api/users/company")
public class CompanyDetailApiController {
    @Autowired
    CompanyDetailService_sangin companyDetailService;
    //private final SecurityUtil securityUtil;

    @GetMapping("/detail/{companyId}")
    public ResponseEntity<Object> detail(@PathVariable String companyId) {
        //String userId =  securityUtil.getCurrentUserId();
        String userId = "test1";
        CompanyDetailVo companyDetail = companyDetailService.getCompanyDetailById(companyId, userId);

        System.out.println("company detail controller " + companyDetail);
        if(companyDetail == null) {
            System.out.println("companyDetailVo is null");
            return ResponseEntity.notFound().build();
        }else{
            System.out.println("companyDetail..success..");
            return ResponseEntity.ok(companyDetail);
        }
    }
}
