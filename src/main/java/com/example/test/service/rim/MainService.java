package com.example.test.service.rim;

import com.example.test.dao.rim.MainDao;
import com.example.test.dto.CompanyDTO;
import com.example.test.dto.rim.main.*;
import com.example.test.security.rim.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainService {
    
    private final MainDao mainDao;
    private final SecurityUtil securityUtil;
    
    // 인기 채용공고 조회
    public List<JobPostDTO> getPopularPosts() {
        return mainDao.findPopularPosts();
    }
    
    // TOP 10 기업 조회
    public List<CompanyDTO> getTopCompanies() {
        return mainDao.findTopCompanies();
    }
    
    // 스크랩 많은 채용공고 조회만 유지
    public List<JobPostDTO> getMostScrapedPosts() {
        return mainDao.findMostScrapedPosts();
    }
    
    // 기존 메인 페이지 전체 데이터 조회 메서드
    public MainResponseDTO getMainPageData() {
        MainResponseDTO response = new MainResponseDTO();
        
        // 공통 섹션 설정
        response.setCommon(getCommonSection());
        
        // 인기 기술스택 설정
        response.setPopularSkills(getPopularSkills());
        
        // 사용자 타입별 섹션 설정 (이미 AOP에서 검증됨)
        UserTypeDTO userSection = new UserTypeDTO();
        String userType = securityUtil.getUserType();  // 검증된 타입
        
        switch (userType) {
            case "guest":
                userSection.setGuest(getGuestSection());
                break;
                
            case "user":
                userSection.setJobSeeker(getJobSeekerSection(securityUtil.getCurrentUserId()));
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
        guestSection.setPopularPosts(mainDao.findPopularPosts());
        guestSection.setTopCompanies(mainDao.findTopCompanies());
        guestSection.setScrapedPosts(mainDao.findMostScrapedPosts());
        return guestSection;
    }
    
    private JobSeekerSectionDTO getJobSeekerSection(String userId) {
        JobSeekerSectionDTO seekerSection = new JobSeekerSectionDTO();
        
        // 대시보드 설정
        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setStats(mainDao.findJobSeekerDashboard(userId));
        dashboard.setRecentApplications(mainDao.findRecentApplications(userId));
        
        seekerSection.setDashboard(dashboard);
        seekerSection.setRecommendedPosts(mainDao.findRecommendedPosts(userId));
        seekerSection.setTopCompanies(mainDao.findTopCompanies());
        seekerSection.setScrapedPosts(mainDao.findMostScrapedPosts());
        seekerSection.setDeadlinePosts(mainDao.findDeadlinePosts(userId));
        
        return seekerSection;
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

    public DashboardDTO getJobSeekerDashboard(String userId) {
        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setStats(mainDao.findJobSeekerDashboard(userId));
        dashboard.setRecentApplications(mainDao.findRecentApplications(userId));
        return dashboard;
    }

    public List<JobPostDTO> getRecommendedPosts(String userId) {
        return mainDao.findRecommendedPosts(userId);
    }

    public List<JobPostDTO> getDeadlinePosts(String userId) {
        return mainDao.findDeadlinePosts(userId);
    }
} 