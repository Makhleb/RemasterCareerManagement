package com.example.test.controller.view.rim;

import com.example.test.security.rim.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.test.security.rim.RequireToken;

@RequestMapping("/")
@Controller
@RequiredArgsConstructor
public class MainViewController {

    private final SecurityUtil securityUtil;

    @GetMapping
    public String index() {

        if(securityUtil.isGeneralUser()) {
            return "redirect:/jobseeker";
        }else if(securityUtil.isCompanyUser()) {
            return "redirect:/company";
        }else{
            return "rim/index";
        }
    }

    @GetMapping("/jobseeker")
    @RequireToken(roles = {"ROLE_USER"})
    public String jobSeekerMain() {
        return "rim/jobseeker/index";
    }

    @GetMapping("/company")
    @RequireToken(roles = {"ROLE_COMPANY"})
    public String companyMain() {
        return "rim/company/index";
    }
}