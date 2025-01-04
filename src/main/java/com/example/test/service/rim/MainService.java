package com.example.test.service.rim;

import com.example.test.dao.rim.MainDao;
import com.example.test.dto.CompanyDTO;
import com.example.test.dto.rim.main.*;
import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.common.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MainService {
    
    private final FileService fileService;
    private final MainDao mainDao;
    private final SecurityUtil securityUtil;

    // 기본 썸네일 이미지 배열 추가
    private static final String[] DEFAULT_THUMBNAILS = {
        "/images/default-job-post-thumbnails/job-post-thumbnail1.png",
        "/images/default-job-post-thumbnails/job-post-thumbnail2.png",
        "/images/default-job-post-thumbnails/job-post-thumbnail3.png",
        "/images/default-job-post-thumbnails/job-post-thumbnail4.png",
        "/images/default-job-post-thumbnails/job-post-thumbnail5.png"
    };
    
    // 기존 메인 페이지 전체 데이터 조회 메서드
    public MainResponseDTO getMainPageData() {
        MainResponseDTO response = new MainResponseDTO();
        
        // 공통 섹션 설정
        response.setCommon(getCommonSection());
        response.setPopularSkills(getPopularSkills());
        
        // 사용자 타입별 섹션 설정
        UserTypeDTO userSection = new UserTypeDTO();
        String userType = securityUtil.getUserType();
        
        switch (userType) {
            case "guest":
                userSection.setGuest(getGuestSection());
                break;
                
            case "user":
                // 구직자의 경우 guest 섹션도 함께 제공
                GuestSectionDTO guestData = getGuestSection();
                JobSeekerSectionDTO seekerData = getJobSeekerSection(securityUtil.getCurrentUserId());
                
                // guest 데이터를 JobSeekerSection에 통합
                seekerData.setPopularPosts(guestData.getPopularPosts());
                seekerData.setScrapedPosts(guestData.getScrapedPosts());
                // topCompanies는 이미 있으므로 제외
                
                userSection.setJobSeeker(seekerData);
                break;
                
            case "company":
                userSection.setCompany(getCompanySection(securityUtil.getCurrentUserId()));
                break;
        }
        
        response.setUserSection(userSection);
        return response;
    }

    private CommonSectionDTO getCommonSection() {
        CommonSectionDTO commonSection = new CommonSectionDTO();
        
        // 메뉴 아이콘 설정
        List<CommonSectionDTO.MenuIconDTO> menuIcons = new ArrayList<>();
        menuIcons.add(createMenuIcon("jobs", "채용공고", "briefcase", "/jobs", false));
        menuIcons.add(createMenuIcon("resume", "이력서관리", "file-text", "/resume", true));
        menuIcons.add(createMenuIcon("applications", "지원현황", "clipboard-list", "/applications", true));
        menuIcons.add(createMenuIcon("scraps", "스크랩", "bookmark", "/scraps", true));
        menuIcons.add(createMenuIcon("search", "검색", "search", "/search", false));
        
        commonSection.setMenuIcons(menuIcons);
        return commonSection;
    }
    
    private GuestSectionDTO getGuestSection() {
        GuestSectionDTO guestSection = new GuestSectionDTO();
        
        // 인기 공고 조회 시 썸네일 처리
        List<JobPostDTO> popularPosts = mainDao.findPopularPosts();
        processJobPostThumbnails(popularPosts);
        guestSection.setPopularPosts(popularPosts);
        
        guestSection.setTopCompanies(mainDao.findTopCompanies());
        
        // 스크랩 많은 공고 조회 시 썸네일 처리
        List<JobPostDTO> scrapedPosts = mainDao.findMostScrapedPosts();
        processJobPostThumbnails(scrapedPosts);
        guestSection.setScrapedPosts(scrapedPosts);
        
        return guestSection;
    }
    
    private JobSeekerSectionDTO getJobSeekerSection(String userId) {
        JobSeekerSectionDTO seekerSection = new JobSeekerSectionDTO();
        
        // 대시보드 설정
        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setStats(mainDao.findJobSeekerDashboard(userId));  // DTO로 직접 매핑
        dashboard.setRecentApplications(mainDao.findRecentApplications(userId));
        seekerSection.setDashboard(dashboard);
        
        // 추천 공고 썸네일 처리
        List<JobPostDTO> recommendedPosts = mainDao.findRecommendedPosts(userId);
        processJobPostThumbnails(recommendedPosts);
        seekerSection.setRecommendedPosts(recommendedPosts);
        
        seekerSection.setTopCompanies(mainDao.findTopCompanies());
        
        // 스크랩 많은 공고 썸네일 처리
        List<JobPostDTO> scrapedPosts = mainDao.findMostScrapedPosts();
        processJobPostThumbnails(scrapedPosts);
        seekerSection.setScrapedPosts(scrapedPosts);
        
        // 마감 임박 공고 썸네일 처리
        List<JobPostDTO> deadlinePosts = mainDao.findDeadlinePosts(userId);
        processJobPostThumbnails(deadlinePosts);
        seekerSection.setDeadlinePosts(deadlinePosts);
        
        return seekerSection;
    }
    
    // 썸네일 처리 공통 메서드
    private void processJobPostThumbnails(List<JobPostDTO> posts) {
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
    
    private CompanySectionDTO getCompanySection(String companyId) {
        CompanySectionDTO companySection = new CompanySectionDTO();
        
        companySection.setProfile(mainDao.findCompanyProfile(companyId));
        companySection.setStats(mainDao.findRecruitmentStats(companyId));
        companySection.setActivePosts(mainDao.findActivePosts(companyId));
        companySection.setRecommendedCandidates(mainDao.findRecommendedCandidates(companyId));
        companySection.setRating(mainDao.findCompanyRating(companyId));
        
        return companySection;
    }
    
    private CommonSectionDTO.MenuIconDTO createMenuIcon(String id, String name, 
            String icon, String link, boolean requireLogin) {
        CommonSectionDTO.MenuIconDTO menuIcon = new CommonSectionDTO.MenuIconDTO();
        menuIcon.setId(id);
        menuIcon.setName(name);
        menuIcon.setIcon(icon);
        menuIcon.setLink(link);
        menuIcon.setRequireLogin(requireLogin);
        return menuIcon;
    }

    public List<PopularSkillDTO> getPopularSkills() {
        return mainDao.findPopularSkills();
    }

    // 랜덤 썸네일 선택 메서드
    private String getRandomDefaultThumbnail() {
        int randomIndex = (int) (Math.random() * DEFAULT_THUMBNAILS.length);
        return DEFAULT_THUMBNAILS[randomIndex];
    }


} 