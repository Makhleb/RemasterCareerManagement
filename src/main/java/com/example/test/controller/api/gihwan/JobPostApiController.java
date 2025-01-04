package com.example.test.controller.api.gihwan;

import com.example.test.dto.JobPostDTO;
import com.example.test.dto.wrapper.JobPostAplcWrapDto;
import com.example.test.dto.wrapper.JobPostWrapDto;
import com.example.test.security.rim.SecurityUtil;
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
    private final SecurityUtil securityUtil;

    public JobPostApiController(JobPostService jobPostService, SecurityUtil securityUtil) {
        this.jobPostService = jobPostService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public int postJobPost(@RequestBody JobPostWrapDto jobPostWrapDto) {
        String compnayId = securityUtil.getCurrentUserId();
        jobPostWrapDto.getJobPost().setCompanyId(compnayId);
        return jobPostService.postJobPost(jobPostWrapDto);
    }

    @GetMapping("/aplc-list")
    public List<JobPostAplcWrapDto> getJobPosts(@RequestParam(required = false) Integer limit) {
        String companyId = securityUtil.getCurrentUserId();
        return jobPostService.selectAllJobPost(companyId, limit);
    }

    @GetMapping("/detail/{detailNo}")
    public JobPostWrapDto getJobPostDetail(@PathVariable int detailNo) {
        return jobPostService.selectDetail(detailNo);
    }

    @PutMapping
    public boolean updateJobPost(@RequestBody JobPostWrapDto jobPostWrapDto) {
        return jobPostService.updatePost(jobPostWrapDto);
    }

    @DeleteMapping("/{jobPostNo}")
    public boolean deleteJobPost(@PathVariable int jobPostNo) throws IOException {
        return jobPostService.deleteJobPost(jobPostNo);
    }
}
