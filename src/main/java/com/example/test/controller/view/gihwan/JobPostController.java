package com.example.test.controller.view.gihwan;

import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created on 2024-12-27 by 최기환
 */
@Controller
@RequestMapping("/mypage/company")
public class JobPostController {
    @GetMapping("/job-post")
    public String jobPost(Model model) {
        String[] gubnList = new String[] {"abc", "def", "ghi"};
        model.addAttribute("skillGubnList", gubnList);
        return "/gihwan/job-post";
    }
}
