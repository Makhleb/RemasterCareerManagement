package com.example.test.controller.view.gihwan;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created on 2024-12-27 by 최기환
 */
@Controller
@RequestMapping("/mypage/company")
public class JobPostController {
    @GetMapping("/job-post")
    public String jobPost(@RequestParam(required = false) Integer post, Model model) {
        model.addAttribute("jobPostNo", post);
        return "/gihwan/job-post";
    }

    @GetMapping("/post-list")
    public String postList(){
        return "/gihwan/mypage-job-post-list";
    }

    @GetMapping("/main")
    public String mypageMain(){
        return "/gihwan/mypage-main";
    }
}
