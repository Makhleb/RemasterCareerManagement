package com.example.test.controller.api.rim;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.test.service.rim.JobPostService;
import com.example.test.service.rim.CompanyService;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {
    
    private final JobPostService jobPostService;
    private final CompanyService companyService;

    // 인기 기술스택별 채용공고
    @GetMapping("/popular-posts")
    public List<Map<String, Object>> getPopularPosts() {
        return jobPostService.findPopularPostsBySkills();
    }

    // TOP 10 기업 (별점 기준)
    @GetMapping("/top-companies")
    public List<Map<String, Object>> getTopCompanies() {
        return companyService.findTopCompaniesByRating();
    }

    // 주목받는 채용공고 (스크랩/조회수 기준)
    @GetMapping("/trending-posts")
    public List<Map<String, Object>> getTrendingPosts(
        @RequestParam(defaultValue = "scrap") String sortBy  // scrap 또는 view
    ) {
        return jobPostService.findTrendingPosts(sortBy);
    }
} 