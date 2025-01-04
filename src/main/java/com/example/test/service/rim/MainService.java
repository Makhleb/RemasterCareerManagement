package com.example.test.service.rim;

import com.example.test.dao.rim.MainDao;
import com.example.test.dao.sangin.UsersLikeDao_sangin;
import com.example.test.dto.CompanyDTO;
import com.example.test.dto.rim.main.*;
import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.common.FileService;
import com.example.test.vo.JobPostDetailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {
    
    private final FileService fileService;
    private final MainDao mainDao;
    private final SecurityUtil securityUtil;
    private final UsersLikeDao_sangin usersLikeDao;

    // 기본 썸네일 이미지 배열 추가
    private static final String[] DEFAULT_THUMBNAILS = {
        "/images/default-job-post-thumbnails/job-post-thumbnail1.png",
        "/images/default-job-post-thumbnails/job-post-thumbnail2.png",
        "/images/default-job-post-thumbnails/job-post-thumbnail3.png",
        "/images/default-job-post-thumbnails/job-post-thumbnail4.png",
        "/images/default-job-post-thumbnails/job-post-thumbnail5.png"
    };

    // 기존 메인 페이지 전체 데이터 조회 메서드
    public MainDTO getMainPageData() {
        MainDTO response = new MainDTO();
        String userType = securityUtil.getCurrentUserRole();
        String userId = null;

        // ROLE_USER인 경우에만 userId를 가져옵니다
        if ("ROLE_USER".equals(userType)) {
            userId = securityUtil.getCurrentUserId();
        }

        log.info("userType: " + userType);

        response.setUserType(userType);
        response.setCommonSection(getCommonSection());
        response.setPopularSkills(getPopularSkills());
        
        // 게스트 섹션 데이터 조회
        MainDTO.GuestSectionDTO guestData = getGuestSection();
        
        // ROLE_USER인 경우에만 스크랩 상태를 설정
        if (userId != null) {
            setScrapStatus(guestData.getPopularPosts(), userId);
            setScrapStatus(guestData.getScrapedPosts(), userId);
        }
        
        switch (userType) {
            case "ROLE_GUEST":
                response.setGuestSection(guestData);
                break;
                
            case "ROLE_USER":
                // 구직자는 게스트 섹션도 함께 표시
                response.setGuestSection(guestData);
                
                // 구직자 전용 데이터 설정
                MainDTO.JobSeekerSectionDTO seekerData = getJobSeekerSection(userId);
                
                // guest 데이터를 JobSeekerSection에도 통합
                seekerData.setPopularPosts(guestData.getPopularPosts());
                seekerData.setTopCompanies(guestData.getTopCompanies());
                seekerData.setScrapedPosts(guestData.getScrapedPosts());
                
                MainDTO.UserSectionDTO userSection = new MainDTO.UserSectionDTO();
                userSection.setJobSeeker(seekerData);
                response.setUserSection(userSection);
                break;
                
            case "ROLE_COMPANY":
                // 기업은 게스트 섹션 표시하지 않음
                MainDTO.UserSectionDTO companySection = new MainDTO.UserSectionDTO();
                companySection.setCompany(getCompanySection(securityUtil.getCurrentUserId()));
                response.setUserSection(companySection);
                break;
        }
        
        return response;
    }

    private MainDTO.CommonSectionDTO getCommonSection() {
        MainDTO.CommonSectionDTO commonSection = new MainDTO.CommonSectionDTO();
        
        // 메뉴 아이콘 설정
        List<MainDTO.CommonSectionDTO.MenuIconDTO> menuIcons = new ArrayList<>();
        menuIcons.add(createMenuIcon("jobs", "채용공고", "briefcase", "/jobs", false));
        menuIcons.add(createMenuIcon("resume", "이력서관리", "file-text", "/resume", true));
        menuIcons.add(createMenuIcon("applications", "지원현황", "clipboard-list", "/applications", true));
        menuIcons.add(createMenuIcon("scraps", "스크랩", "bookmark", "/scraps", true));
        menuIcons.add(createMenuIcon("search", "검색", "search", "/search", false));
        
        commonSection.setMenuIcons(menuIcons);
        return commonSection;
    }
    
    private MainDTO.GuestSectionDTO getGuestSection() {
        MainDTO.GuestSectionDTO guestSection = new MainDTO.GuestSectionDTO();
        
        // 인기 공고 조회 시 썸네일 처리
        List<MainDTO.JobPostDTO> popularPosts = mainDao.findPopularPosts();
        processJobPostThumbnails(popularPosts);
        guestSection.setPopularPosts(popularPosts);
        
        guestSection.setTopCompanies(mainDao.findTopCompanies());
        
        // 스크랩 많은 공고 조회 시 썸네일 처리
        List<MainDTO.JobPostDTO> scrapedPosts = mainDao.findMostScrapedPosts();
        processJobPostThumbnails(scrapedPosts);
        guestSection.setScrapedPosts(scrapedPosts);
        
        return guestSection;
    }
    
    private MainDTO.JobSeekerSectionDTO getJobSeekerSection(String userId) {
        MainDTO.JobSeekerSectionDTO seekerSection = new MainDTO.JobSeekerSectionDTO();
        
        // 대시보드 설정
        MainDTO.DashboardDTO dashboard = new MainDTO.DashboardDTO();
        dashboard.setStats(mainDao.findJobSeekerDashboard(userId));  // DTO로 직접 매핑
        dashboard.setRecentApplications(mainDao.findRecentApplications(userId));
        seekerSection.setDashboard(dashboard);
        
        // 추천 공고 썸네일 처리
        List<MainDTO.JobPostDTO> recommendedPosts = mainDao.findRecommendedPosts(userId);
        processJobPostThumbnails(recommendedPosts);
        seekerSection.setRecommendedPosts(recommendedPosts);
        
        seekerSection.setTopCompanies(mainDao.findTopCompanies());
        
        // 스크랩 많은 공고 썸네일 처리
        List<MainDTO.JobPostDTO> scrapedPosts = mainDao.findMostScrapedPosts();
        processJobPostThumbnails(scrapedPosts);
        seekerSection.setScrapedPosts(scrapedPosts);
        
        // 마감 임박 공고 썸네일 처리
        List<MainDTO.JobPostDTO> deadlinePosts = mainDao.findDeadlinePosts(userId);
        processJobPostThumbnails(deadlinePosts);
        seekerSection.setDeadlinePosts(deadlinePosts);
        
        return seekerSection;
    }
    
    // 썸네일 처리 공통 메서드
    private void processJobPostThumbnails(List<MainDTO.JobPostDTO> posts) {
        if (posts == null) return;
        
        posts.forEach(post -> {
            String thumbnail = fileService.loadImage(
                "POST_THUMBNAIL", 
                String.valueOf(post.getJobPostNo())
            );
            
            if (thumbnail != null) {
                post.setPostThumbnail(thumbnail);
            } else {
                post.setPostThumbnail(getRandomDefaultThumbnail());
            }
        });
    }
    
    private MainDTO.CompanySectionDTO getCompanySection(String companyId) {
        MainDTO.CompanySectionDTO companySection = new MainDTO.CompanySectionDTO();
        
        companySection.setProfile(mainDao.findCompanyProfile(companyId));
        companySection.setStats(mainDao.findRecruitmentStats(companyId));
        companySection.setActivePosts(mainDao.findActivePosts(companyId));
        companySection.setRecommendedCandidates(mainDao.findRecommendedCandidates(companyId));
        companySection.setRating(mainDao.findCompanyRating(companyId));
        
        return companySection;
    }
    
    private MainDTO.CommonSectionDTO.MenuIconDTO createMenuIcon(String id, String name, 
            String icon, String link, boolean requireLogin) {
        MainDTO.CommonSectionDTO.MenuIconDTO menuIcon = new MainDTO.CommonSectionDTO.MenuIconDTO();
        menuIcon.setId(id);
        menuIcon.setName(name);
        menuIcon.setIcon(icon);
        menuIcon.setLink(link);
        menuIcon.setRequireLogin(requireLogin);
        return menuIcon;
    }

    public List<MainDTO.PopularSkillDTO> getPopularSkills() {
        return mainDao.findPopularSkills();
    }

    // 랜덤 썸네일 선택 메서드
    private String getRandomDefaultThumbnail() {
        int randomIndex = (int) (Math.random() * DEFAULT_THUMBNAILS.length);
        return DEFAULT_THUMBNAILS[randomIndex];
    }

    public MainDTO.CompanyProfileDTO getCompanyProfile(String companyId) {
        return mainDao.findCompanyProfile(companyId);
    }

    public MainDTO.RecruitmentStatsDTO getRecruitmentStats(String companyId) {
        return mainDao.findRecruitmentStats(companyId);
    }

    public MainDTO.CompanyRatingDTO getCompanyRating(String companyId) {
        return mainDao.findCompanyRating(companyId);
    }

    private void setScrapStatus(List<MainDTO.JobPostDTO> posts, String userId) {
        if (userId == null || posts == null || posts.isEmpty()) {
            return;
        }

        // 현재 사용자의 스크랩한 공고 목록
        List<JobPostDetailVo> scrapedPosts = usersLikeDao.jobPostLike(userId);
        
        // jobPostNo만 추출하여 Set으로 변환 (검색 성능 향상을 위해)
        Set<Integer> scrapedPostNos = scrapedPosts.stream()
            .map(JobPostDetailVo::getJobPostNo)
            .collect(Collectors.toSet());
        
        // 각 게시물의 스크랩 여부를 설정합니다 (0 또는 1로 설정)
        posts.forEach(post -> {
            post.setIsScraped(scrapedPostNos.contains(post.getJobPostNo()) ? 1 : 0);
        });
    }
} 