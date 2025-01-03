package com.example.test.controller.api.rim;

import com.example.test.dto.CompanyDTO;
import com.example.test.dto.rim.main.MainResponseDTO;
import com.example.test.dto.rim.main.JobPostDTO;
import com.example.test.service.rim.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    // 메인 페이지 전체 데이터 조회
    @GetMapping("/data")
    public MainResponseDTO getMainPageData() {
        return mainService.getMainPageData();
    }

    // 인기 채용공고 조회
    @GetMapping("/popular-posts")
    public List<JobPostDTO> getPopularPosts() {
        return mainService.getPopularPosts();
    }

    // TOP 10 기업 조회
    @GetMapping("/top-companies")
    public List<CompanyDTO> getTopCompanies() {
        return mainService.getTopCompanies();
    }

    // 스크랩 많은 채용공고 조회
    @GetMapping("/most-scraped")
    public List<JobPostDTO> getMostScrapedPosts() {
        return mainService.getMostScrapedPosts();
    }
} 