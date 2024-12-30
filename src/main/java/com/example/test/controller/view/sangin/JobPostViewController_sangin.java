package com.example.test.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/users/job-post")
public class JobPostViewController_sangin {
    @GetMapping("/list")
    public String jobPostList() {
        System.out.println("list...");
        return "/sangin/job-post-list";
    }
    @GetMapping("/detail/{jobPostNo}")
    public String jobPostDetail(@PathVariable("jobPostNo") int jobPostNo, Model model) {
        model.addAttribute("jobPostNo", jobPostNo);
        System.out.println("detail..." + jobPostNo);
        return "/sangin/job-post-detail";
    }

}
