package com.example.test.dto.rim.main;

import lombok.Data;
import java.util.List;

@Data
public class CompanySectionDTO {
    private CompanyProfileDTO profile;
    private RecruitmentStatsDTO stats;
    private List<JobPostDTO> activePosts;
    private List<CandidateDTO> recommendedCandidates;
    private CompanyRatingDTO rating;
} 