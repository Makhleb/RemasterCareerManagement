package com.example.test.dto.rim.main;

import com.example.test.dto.CompanyDTO;
import lombok.Data;
import java.util.List;
import java.util.Map;

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
        private CompanyRatingDTO rating;
        private RecruitmentStatsDTO stats;
        private List<JobPostDTO> activePosts;
        private List<CandidateDTO> recommendedCandidates;
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
        private int activePostings;        // 진행중 공고
        private int waitingResumes;        // 신규 지원자
        private int totalApplications30Days; // 최근 30일 총 지원자
        private int passCount30Days;       // 최근 30일 합격
        private int failCount30Days;       // 최근 30일 불합격
        private int waitingCount30Days;    // 최근 30일 검토중
        private List<Map<String, Object>> dailyApplications; // 일별 지원자 수 통계
        private List<RecentPostStatsDTO> recentPostStats;   // 최근 공고별 지원자 통계
    }

    @Data
    public static class RecentPostStatsDTO {
        private int jobPostNo;         // 공고 번호
        private String title;          // 공고 제목
        private int totalApplicants;   // 총 지원자 수
        private int waitingCount;      // 검토중인 지원자 수
        private String startDate;      // 공고 시작일
    }

    @Data
    public static class CompanyRatingDTO {
        private double averageRating;      // 평균 평점
        private int reviewCount;           // 총 리뷰 수
        private List<Integer> ratingDistribution; // 평점 분포 [1점개수, 2점개수, ...]
        private List<ReviewDTO> recentReviews;    // 최근 리뷰 3개
    }

    @Data
    public static class ReviewDTO {
        private int score;          // 평점
        private String userId;      // 익명처리된 ID
        private String scoreDate;   // 평점 등록일
    }

    @Data
    public static class CandidateDTO {
        private String name;        // 익명처리된 이름
        private String education;   // 학력
        private String career;      // 경력여부
        private List<String> skills; // 보유 기술
        private double matchRate;   // 매칭률
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
        private int isScraped;  // 0 또는 1로 저장
        private String postThumbnail;
        private int totalApplicants;  // 총 지원자 수
        private int unreadCount;      // 미열람 이력서 수
        private int waitingCount;     // 검토중인 지원자 수
        private int viewCount;        // 조회수
        private int daysLeft;         // 마감까지 남은 일수
    }

    @Data
    public static class PopularSkillDTO {
        private String skillName;
        private int postCount;
    }
} 