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
        String userType = (String)securityUtil.getCurrentUserInfo().get("type");
        
        // 사용자 타입에 따라 적절한 메인 페이지로 리다이렉트
        return switch (userType) {
            case "user" -> "redirect:/jobseeker";
            case "company" -> "redirect:/company";
            default -> "rim/index";  // 비회원용 메인 페이지
        };
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