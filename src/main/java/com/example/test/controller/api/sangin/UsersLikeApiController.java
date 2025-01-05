package com.example.test.controller.api.sangin;

import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.sangin.UsersLikeService_sangin;
import com.example.test.vo.CompanyDetailVo;
import com.example.test.vo.JobPostDetailVo;
import com.example.test.vo.LikeCountVo;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 2024-12-31 by 한상인
 */
@Controller
@RequestMapping("/api/users/like")
@RequiredArgsConstructor
@Slf4j
public class UsersLikeApiController {
    @Autowired
    UsersLikeService_sangin usersLikeService;

    private final SecurityUtil securityUtil;

    @GetMapping("/cl")
    public ResponseEntity<Object> companyLike() {
        String userId =  securityUtil.getCurrentUserId();
//        String userId = "test1";
        List<CompanyDetailVo> companyList = usersLikeService.companyLikeWithPosts(userId);
        if (companyList != null && !companyList.isEmpty()) {
            return ResponseEntity.ok(companyList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/jpl")
    public ResponseEntity<Object> jobPostLike() {
        String userId =  securityUtil.getCurrentUserId();
//        String userId = "test1";
        List<JobPostDetailVo> jobPostList = usersLikeService.jobPostLike(userId);
        if (jobPostList != null && !jobPostList.isEmpty()) {
            return ResponseEntity.ok(jobPostList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<Object> jobPostSummary() {
        String userId =  securityUtil.getCurrentUserId();
//        String userId = "test1";
        LikeCountVo likeCountVo = usersLikeService.summary(userId);
        if (likeCountVo != null) {
            return ResponseEntity.ok(likeCountVo);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/jpl/add")
    public ResponseEntity<Object> jplAdd(@RequestParam Integer jobPostNo) {
        String userId =  securityUtil.getCurrentUserId();
//        String userId = "test1";
        int result = usersLikeService.jplAdd(userId, jobPostNo);
        if (result == 1) {
            return ResponseEntity.ok("굿!");
        } else {
            return ResponseEntity.badRequest().body("낫굿 ㅠ");
        }
    }


    @GetMapping("/jpl/remove")
    public ResponseEntity<Object> jplRemove(@RequestParam Integer jobPostNo) {
        String userId =  securityUtil.getCurrentUserId();
//        String userId = "test1";
        int result = usersLikeService.jplRemove(userId, jobPostNo);
        if (result == 1) {
            return ResponseEntity.ok("굿!");
        } else {
            return ResponseEntity.badRequest().body("낫굿 ㅠ");
        }
    }

    @GetMapping("/cl/add")
    public ResponseEntity<Object> clAdd(@RequestParam String companyId) {
        String userId =  securityUtil.getCurrentUserId();
//        String userId = "test1";
        int result = usersLikeService.clAdd(userId, companyId);
        if (result == 1) {
            return ResponseEntity.ok("굿!");
        } else {
            return ResponseEntity.badRequest().body("낫굿 ㅠ");
        }
    }


    @GetMapping("/cl/remove")
    public ResponseEntity<Object> clRemove(@RequestParam String companyId) {
        String userId =  securityUtil.getCurrentUserId();
//        String userId = "test1";
        int result = usersLikeService.clRemove(userId, companyId);
        if (result == 1) {
            return ResponseEntity.ok("굿!");
        } else {
            return ResponseEntity.badRequest().body("낫굿 ㅠ");
        }
    }


}
