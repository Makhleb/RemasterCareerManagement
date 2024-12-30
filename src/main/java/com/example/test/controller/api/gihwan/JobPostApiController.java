package com.example.test.controller.api.gihwan;

import com.example.test.dao.gihwan.GubnDao;
import com.example.test.dto.GubnDTO;
import com.example.test.dto.wrapper.JobPostWrapDto;
import com.example.test.service.gihwan.JobPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        //todo 로그인 회원가입 완성시 고치기
        jobPostWrapDto.getJobPost().setCompanyId("testcompany1");
        return jobPostService.postJobPost(jobPostWrapDto);
    }

    @PostMapping("/image")
    public boolean uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("post-no") int postNo) {
        try {
            String uploadDir = "";

            uploadDir = System.getProperty("user.dir") + "/upload" + "/job-post" + "/" + postNo;
            File directory = new File(uploadDir);

            if (!directory.exists()) {
                boolean isCreated = directory.mkdirs();  // 디렉토리 생성 여부 확인
                if (isCreated) {
                    System.out.println("업로드 디렉터리가 생성되었습니다: " + uploadDir);
                } else {
                    throw new IOException("디렉터리 생성 실패");
                }
            }

            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
