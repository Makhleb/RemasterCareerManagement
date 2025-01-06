package com.example.test.controller.view.rim;

import com.example.test.security.rim.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/mypage")
@RequiredArgsConstructor
public class UserMypageViewController {

    private final SecurityUtil securityUtil;

    /**
     * 회원정보 수정 페이지
     */
    @GetMapping
    public String mypage() {
        return "rim/user/mypage";
    }
    /**
     * 기술스택 매칭 페이지
     */
    @GetMapping("/skill-matching")
    public String skillMatching() {
        return "rim/user/mypage-skill-matching";
    }

    /**
     * 지원 현황 페이지
     */
    @GetMapping("/applications")
    public String applications() {
        if (!securityUtil.isGeneralUser()) {
            return "redirect:/login";
        }
        return "rim/user/mypage";
    }

    @GetMapping("/info")
    public String info() {
        if (!securityUtil.isGeneralUser()) {
            return "redirect:/login";
        }
        return "rim/user/info";
    }
} 