package com.example.test.controller.api.gihwan;

import com.example.test.dto.JobPostDTO;
import com.example.test.dto.wrapper.JobPostAplcWrapDto;
import com.example.test.dto.wrapper.JobPostWrapDto;
import com.example.test.service.gihwan.JobPostService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        //todo 로그인 회원가입 완성시 고치기
        jobPostWrapDto.getJobPost().setCompanyId("testcompany1");
        return jobPostService.postJobPost(jobPostWrapDto);
    }

    @GetMapping("/{companyId}")
    public List<JobPostAplcWrapDto> getJobPosts(@PathVariable String companyId) {
        return jobPostService.selectAllJobPost(companyId);
    }
}
