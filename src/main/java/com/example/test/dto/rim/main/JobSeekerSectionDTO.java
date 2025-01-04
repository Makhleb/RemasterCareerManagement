package com.example.test.dto.rim.main;

import com.example.test.dto.CompanyDTO;
import lombok.Data;
import java.util.List;

@Data
public class JobSeekerSectionDTO {
    private DashboardDTO dashboard;
    private List<JobPostDTO> recommendedPosts;  // 맞춤 추천 공고
    private List<CompanyDTO> topCompanies;      // TOP 10 기업
    private List<JobPostDTO> scrapedPosts;      // 스크랩 많은 공고
    private List<JobPostDTO> deadlinePosts;     // 마감 임박 공고
    private List<JobPostDTO> popularPosts;    // 인기 공고
} 