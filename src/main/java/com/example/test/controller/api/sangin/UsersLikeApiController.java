package com.example.test.controller.api.sangin;

import com.example.test.dto.CompanyDTO;
import com.example.test.service.sangin.UsersLikeService_sangin;
import com.example.test.vo.JobPostDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created on 2024-12-31 by 한상인
 */
@Controller
@RequestMapping("/api/users/like")
public class UsersLikeApiController {
    @Autowired
    UsersLikeService_sangin usersLikeService;

    @GetMapping("/cl")
    public ResponseEntity<Object> jobPostJpl() {
        System.out.println("api/cl...");
        String userId = "test1";
        List<CompanyDTO> companyList = usersLikeService.companyLike(userId);
        if (companyList != null && !companyList.isEmpty()) {
            return ResponseEntity.ok(companyList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/jpl")
    public ResponseEntity<Object> jobPostLike() {
        System.out.println("api/jpl...");
        String userId = "test1";
        List<JobPostDetailVo> jobPostList = usersLikeService.jobPostLike(userId);
        if (jobPostList != null && !jobPostList.isEmpty()) {
            return ResponseEntity.ok(jobPostList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
