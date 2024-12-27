package com.example.test.controller.api.gyeonguk;

import com.example.test.dto.ResumeDTO;
import com.example.test.service.gyeonguk.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2024-12-27 by 이경욱
 * 이력서 등록을 위한 REST 컨트롤러
 */
@RestController
@RequestMapping("/api/users/resume")
@RequiredArgsConstructor
public class ResumeRegistApiController {

    private final ResumeService resumeService;

    /**
     * 유저 정보를 기반으로 이력서를 등록합니다.
     * @param resumeDTO 이력서 등록에 필요한 데이터
     * @return 등록 결과 메시지
     */
    @PostMapping("/register")
    public String registerResume(@RequestBody ResumeDTO resumeDTO) {
        // 서비스 레이어를 호출하여 이력서 등록 처리
        resumeService.registerResume(resumeDTO);
        return "이력서가 성공적으로 등록되었습니다.";
    }

    /**
     * 유저 정보를 조회합니다.
     * @param userId 조회할 유저의 ID
     * @return 유저 정보 DTO
     */
    @GetMapping("/user/{userId}")
    public ResumeDTO getUserInfo(@PathVariable String userId) {
        // 서비스 레이어를 호출하여 유저 정보 조회
        return resumeService.getUserInfo(userId);
    }
}
