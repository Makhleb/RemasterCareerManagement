package com.example.test.controller.view.rim;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Created on 2024-12-30 by 구경림
 */
@Controller
public class AuthViewController {
    /**
     * 회원가입 페이지
     */
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/company/signup")
    public String companySignup() {
        return "company/signup";
    }
}
