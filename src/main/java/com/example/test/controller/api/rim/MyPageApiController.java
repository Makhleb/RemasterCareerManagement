package com.example.test.controller.api.rim;

import com.example.test.dto.common.ApiResponse;
import com.example.test.dto.rim.mypage.ApplicationStatusDTO;
import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.rim.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/mypage/api/applications")
@RequiredArgsConstructor
public class MyPageApiController {

    private final ApplicationService applicationService;
    private final SecurityUtil securityUtil;

    /**
     * 지원 현황 통계 조회
     */
    @GetMapping("/stats")
    public ApiResponse<ApplicationStatusDTO.ApplicationStats> getStats() {
        String userId = securityUtil.getCurrentUserId();
        ApplicationStatusDTO status = applicationService.getApplicationStatus(userId);
        return ApiResponse.success(status.getStats());
    }

    /**
     * 최근 지원 내역 조회
     */
    @GetMapping("/recent")
    public ApiResponse<ApplicationStatusDTO> getRecentApplications(
            @RequestParam(defaultValue = "10") int limit
    ) {
        String userId = securityUtil.getCurrentUserId();
        ApplicationStatusDTO status = applicationService.getRecentApplications(userId, limit);
        return ApiResponse.success(status);
    }

    /**
     * 기업 평점 등록
     */
    @PostMapping("/companies/scores")
    public ApiResponse<Void> createScore(@RequestBody ScoreRequest request) {
        try {
            String userId = securityUtil.getCurrentUserId();
            applicationService.createScore(
                    userId,
                    request.getCompanyId(),
                    request.getScore()
            );
            return ApiResponse.success(null, "평점이 성공적으로 등록되었습니다.");
        } catch (IllegalStateException e) {
            // GlobalExceptionHandler에서 처리될 수 있도록 그대로 throw
            throw e;
        }
    }

    /**
     * 기업 평점 수정
     */
    @PutMapping("/companies/scores")
    public ApiResponse<Void> updateScore(@RequestBody ScoreRequest request) {
        try {
            String userId = securityUtil.getCurrentUserId();
            applicationService.updateScore(
                    userId,
                    request.getCompanyId(),
                    request.getScore()
            );
            return ApiResponse.success(null, "평점이 성공적으로 수정되었습니다.");
        } catch (IllegalStateException e) {
            // GlobalExceptionHandler에서 처리될 수 있도록 그대로 throw
            throw e;
        }
    }

    // 평점 요청 DTO
    public static class ScoreRequest {
        private String companyId;
        private int score;

        public String getCompanyId() {
            return companyId;
        }

        public int getScore() {
            return score;
        }
    }
} 