package com.example.test.controller.api.sangin;

import com.example.test.dto.JobPostDTO;
import com.example.test.dto.JobPostSkillDTO;
import com.example.test.service.sangin.JobPostService_sangin;
import com.example.test.vo.JobPostDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created on 2024-12-27 by 한상인
 */
@Controller
@RequestMapping("/api/users/job-post")
public class JobPostApiController_sangin {

    @Autowired
    JobPostService_sangin jobPostService;
    @GetMapping("/list/matching")
    @ResponseBody
    public ResponseEntity<Object> jobPostList2() {
        List<JobPostDTO> jobPostList = jobPostService.getJobPostMatching();
        if (jobPostList != null && !jobPostList.isEmpty()) {
            return ResponseEntity.ok(jobPostList);
        } else {
            return ResponseEntity.noContent().build();//데이터가 없을 때 반환하는 코드
        }
    }
    @GetMapping("/list/all")
    @ResponseBody
    public ResponseEntity<Object> jobPostList1() {
        List<JobPostDTO> jobPostList = jobPostService.getJobPostAll();
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
        JobPostDetailVo jobPost = jobPostService.getJobPost(jobPostNo);
        List<JobPostSkillDTO> skillList = jobPostService.getJobPostSkill(jobPostNo);
        jobPost.setSkillList(skillList);
        //기술스택도 같이 전달해야하는데 어떻게 전달할까나~~
        //vo를 하나 만들까??
        if (jobPost != null) {
            return ResponseEntity.ok(jobPost);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
