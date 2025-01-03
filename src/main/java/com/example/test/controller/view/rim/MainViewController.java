package com.example.test.controller.view.rim;

import com.example.test.security.rim.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
@RequiredArgsConstructor
public class MainViewController {

    private final SecurityUtil securityUtil;

    @GetMapping
    public String index() {
            return "rim/index";
    }

    @GetMapping("/jobseeker")
    public String jobSeekerMain() {
        return "rim/jobseeker/index";
    }

    @GetMapping("/company")
    public String companyMain() {
        return "rim/company/index";
    }
}