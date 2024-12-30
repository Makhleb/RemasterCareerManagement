package com.example.test.controller.view.sangin;

import com.example.test.dto.JobPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class JobPostListController {
    @GetMapping("/test")
    public String test() {
        System.out.println("test...");
        return "/sangin/job-post-list";
    }


}
