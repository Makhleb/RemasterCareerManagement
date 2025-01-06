package com.example.test.controller.api.rim;

import com.example.test.security.rim.RequireToken;
import com.example.test.service.rim.ResumeMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users/resume")
@RequiredArgsConstructor
public class ResumeMatchingController {

    private final ResumeMatchingService resumeMatchingService;

    /**
     * 사용자의 이력서 목록 조회
     */
    @GetMapping("/compact-resume")
    @RequireToken(roles = {"ROLE_USER"})
    public ResponseEntity<List<Map<String, Object>>> getCompactResumes() {
        return ResponseEntity.ok(resumeMatchingService.getCompactResumes());
    }

    /**
     * 특정 이력서와 매칭되는 채용공고 목록 조회
     */
    @GetMapping("/skill-matching/{resumeNo}")
    @RequireToken(roles = {"ROLE_USER"})
    public ResponseEntity<List<Map<String, Object>>> getMatchingJobPosts(
            @PathVariable int resumeNo
    ) {
        return ResponseEntity.ok(resumeMatchingService.getMatchingJobPosts(resumeNo));
    }
} 