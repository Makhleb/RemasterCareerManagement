package com.example.test.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/view/users/job-post")
public class JobPostViewController_sangin {
    @GetMapping("/list")
    public String jobPostList(@RequestParam(value = "keyword", required = false)String keyword, Model model) {
        System.out.println("list...");
        System.out.println(keyword);
        return "/sangin/job-post-list";
    }
    @GetMapping("/detail/{jobPostNo}")
    public String jobPostDetail(@PathVariable("jobPostNo") int jobPostNo, Model model) {
        model.addAttribute("jobPostNo", jobPostNo);
        System.out.println("detail..." + jobPostNo);
        return "/sangin/job-post-detail";
    }

}
