package com.example.test.controller.api.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created on 2024-12-27 by 한상인
 */
@Controller
public class PostListController {
    @GetMapping("/users/job-post-list")
    public String jobPostList(){
        return "job-post-list";
    }
}
