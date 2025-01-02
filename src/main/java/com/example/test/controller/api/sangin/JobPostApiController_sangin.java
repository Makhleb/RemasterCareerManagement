package com.example.test.controller.api.sangin;

import com.example.test.dto.*;
import com.example.test.service.sangin.JobPostService_sangin;
import com.example.test.vo.JobPostDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 2024-12-27 by 한상인
 */
@Controller
@RequestMapping("/api/users/job-post")
public class JobPostApiController_sangin {

    @Autowired
    JobPostService_sangin jobPostService;

    //private final SecurityUtil securityUtil;

    @GetMapping("/list/matching")
    @ResponseBody
    public ResponseEntity<Object> jobPostList2() {
        //String userId =  securityUtil.getCurrentUserId();
        String userId = "test1";

        List<JobPostDetailVo> jobPostList = jobPostService.getJobPostMatching(userId);
        if (jobPostList != null && !jobPostList.isEmpty()) {
            return ResponseEntity.ok(jobPostList);
        } else {
            return ResponseEntity.noContent().build();//데이터가 없을 때 반환하는 코드
        }
    }
    @GetMapping("/list/all")
    @ResponseBody
    public ResponseEntity<Object> jobPostList1() {

//      String userId =  securityUtil.getCurrentUserId();
        String userId = "test1";
        List<JobPostDetailVo> jobPostList = jobPostService.getJobPostAll(userId);
        if (jobPostList != null && !jobPostList.isEmpty()) {
            return ResponseEntity.ok(jobPostList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/detail/{jobPostNo}")
    @ResponseBody
    public ResponseEntity<Object> jobPostDetail(@PathVariable("jobPostNo") Integer jobPostNo) {
        System.out.println("api/detail.......");
        //      String userId =  securityUtil.getCurrentUserId();
        String userId = "test1";
        JobPostDetailVo jobPost = jobPostService.getJobPost(userId, jobPostNo);
        List<JobPostSkillDTO> skillList = jobPostService.getJobPostSkill(jobPostNo);
        List<BenefitDTO> benefitList = jobPostService.getJobPostBenefit(jobPostNo);
        jobPost.setSkillList(skillList);
        jobPost.setBenefitList(benefitList);
        //기술스택도 같이 전달해야하는데 어떻게 전달할까나~~
        //vo를 하나 만들까??
        if (jobPost != null) {
            return ResponseEntity.ok(jobPost);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/resume/list")
    @ResponseBody
    public ResponseEntity<Object> ResumeList() {
        //      String userId =  securityUtil.getCurrentUserId();
        String userId = "test1";
        List<ResumeDTO> resumeList = jobPostService.getResumeList(userId);
        if (resumeList != null && !resumeList.isEmpty()) {
            return ResponseEntity.ok(resumeList);
        }else {
            return ResponseEntity.noContent().build();
        }
    }
    @PostMapping("/resume/regist")
    @ResponseBody
    public ResponseEntity<Object> ResumeRegist(@RequestBody AplcHstrDTO aplcHstrDTO) {

        System.out.println("api/resume/regist......." + aplcHstrDTO);
        int result = jobPostService.registAplcHstr(aplcHstrDTO);
        if (result == 1) {
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.noContent().build();
        }
    }

}
