package com.example.test.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created on 2024-12-31 by 한상인
 */
@Controller
@RequestMapping("/view/users/like")
public class UsersLikeViewController {
    @GetMapping("/cl")
    public String jobPostJpl() {
        System.out.println("view/cl...");
        return "/sangin/company-like";
    }

    @GetMapping("/jpl")
    public String jobPostLike() {
        System.out.println("view/jpl...");
        String userId = "test1";
        return "/sangin/job-post-like";
    }
}
