package com.example.test.dto.rim.main;

import com.example.test.dto.CompanyDTO;
import lombok.Data;
import java.util.List;

@Data
public class MainDTO {
    private UserSectionDTO userSection;
    private GuestSectionDTO guestSection;
    private CommonSectionDTO commonSection;
    private List<PopularSkillDTO> popularSkills;
    private String userType;  // ROLE_GUEST, ROLE_USER, ROLE_COMPANY

    @Data
    public static class CommonSectionDTO {
        private List<MenuIconDTO> menuIcons;
        
        @Data
        public static class MenuIconDTO {
            private String id;
            private String name;
            private String icon;
            private String link;
            private boolean requireLogin;
        }
    }

    @Data
    public static class UserSectionDTO {
        private JobSeekerSectionDTO jobSeeker;
        private CompanySectionDTO company;
    }

    @Data
    public static class CompanySectionDTO {
        private CompanyProfileDTO profile;
        private RecruitmentStatsDTO stats;
        private List<JobPostDTO> activePosts;
        private List<CandidateDTO> recommendedCandidates;
        private CompanyRatingDTO rating;
    }

    @Data
    public static class CompanyProfileDTO {
        private String companyId;
        private String companyName;
        private String companyImage;
        private String companyAddress;
        private String companyWebsite;
        private Integer companyEmployee;
        private Long companyProfit;
    }

    @Data
    public static class RecruitmentStatsDTO {
        private int activePostings;
        private int unreadResumes;
        private int totalApplications30Days;
        private int passCount30Days;
        private int failCount30Days;
        private int waitingCount30Days;
    }

    @Data
    public static class CompanyRatingDTO {
        private double averageRating;
        private int reviewCount;
        private List<Integer> ratingDistribution; // [1점개수, 2점개수, ...]
        private List<ReviewDTO> recentReviews;
    }

    @Data
    public static class ReviewDTO {
        private int score;
        private String userId;  // 익명처리된 ID
        private String scoreDate; 
    }

    @Data
    public static class CandidateDTO {
        private String userId;
        private String name;
        private String education;
        private String career;
        private List<String> skills;
        private double matchRate;
    }

    @Data
    public static class JobSeekerSectionDTO {
        private DashboardDTO dashboard;
        private List<JobPostDTO> recommendedPosts;  // 맞춤 추천 공고
        private List<CompanyDTO> topCompanies;      // TOP 10 기업
        private List<JobPostDTO> scrapedPosts;      // 스크랩 많은 공고
        private List<JobPostDTO> deadlinePosts;     // 마감 임박 공고
        private List<JobPostDTO> popularPosts;      // 인기 공고
    }

    @Data
    public static class DashboardDTO {
        private StatsDTO stats;
        private List<ApplicationDTO> recentApplications;

        @Data
        public static class StatsDTO {
            private int inProgress;  // 진행 중
            private int accepted;    // 합격
            private int rejected;    // 불합격
            private int total;       // 총 지원 수
        }

        @Data
        public static class ApplicationDTO {
            private String companyName;  // 기업명
            private String postTitle;    // 공고 제목
            private String applyDate;    // 지원일
            private String passYn;       // 합격여부 (W: 대기중, Y: 합격, N: 불합격)
        }
    }

    @Data
    public static class GuestSectionDTO {
        private List<JobPostDTO> popularPosts;      // 인기 공고
        private List<CompanyDTO> topCompanies;      // TOP 10 기업
        private List<JobPostDTO> scrapedPosts;      // 스크랩 많은 공고
    }

    @Data
    public static class JobPostDTO {
        private int jobPostNo;
        private String companyName;
        private String companyImage;
        private String title;
        private String location;
        private String career;
        private String education;
        private String jobSalary;
        private String endDate;
        private List<String> skillCodes;  // 기술스택 (랜덤 2개)
        private List<String> benefits;    // 복리후생 (랜덤 1개)
        private int scrapCount;
        private boolean isScraped;
        private String postThumbnail;
    }

    @Data
    public static class PopularSkillDTO {
        private String skillName;
        private int postCount;
    }
} 