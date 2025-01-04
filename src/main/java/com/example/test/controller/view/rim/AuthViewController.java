package com.example.test.controller.view.rim;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Created on 2024-12-30 by 구경림
 */
@Controller
@RequestMapping("/")
public class AuthViewController {
    /**
     * 회원가입 페이지
     */
    @GetMapping("/signup")
    public String signup() {
        return "rim/signup";
    }

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String login() {
        return "rim/login";
    }

    @GetMapping("/company/signup")
    public String companySignup() {
        return "rim/company/signup";
    }

    @GetMapping("/find-password")
    public String findPassword() {
        return "rim/find_password";
    }
}
