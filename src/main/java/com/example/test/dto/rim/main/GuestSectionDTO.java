package com.example.test.dto.rim.main;

import com.example.test.dto.CompanyDTO;
import lombok.Data;
import java.util.List;

@Data
public class GuestSectionDTO {
    private List<JobPostDTO> popularPosts;      // 인기 채용공고
    private List<CompanyDTO> topCompanies;      // TOP 10 기업
    private List<JobPostDTO> scrapedPosts;      // 스크랩 많은 공고
}