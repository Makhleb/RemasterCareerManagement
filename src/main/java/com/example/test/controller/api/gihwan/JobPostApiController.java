package com.example.test.controller.api.gihwan;

import com.example.test.dao.gihwan.GubnDao;
import com.example.test.dto.GubnDTO;
import com.example.test.dto.wrapper.JobPostWrapDto;
import com.example.test.service.gihwan.JobPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2024-12-27 by 최기환
 */
@RestController
@RequestMapping("/api/companies/job-post")
public class JobPostApiController {
    private final JobPostService jobPostService;

    public JobPostApiController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @PostMapping
    public int postJobPost(@RequestBody JobPostWrapDto jobPostWrapDto) {
        return jobPostService.postJobPost(jobPostWrapDto);
    }
}
