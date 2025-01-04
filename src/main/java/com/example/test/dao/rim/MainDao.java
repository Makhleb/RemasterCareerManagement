package com.example.test.dao.rim;

import com.example.test.dto.CompanyDTO;
import com.example.test.dto.rim.main.MainDTO;
import com.example.test.dto.rim.main.MainDTO.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MainDao {
    // 인기 채용공고
    List<JobPostDTO> findPopularPosts();
    
    // TOP 10 기업
    List<CompanyDTO> findTopCompanies();
    
    // 스크랩 많은 채용공고
    List<JobPostDTO> findMostScrapedPosts();
    
    // 구직자 대시보드
    DashboardDTO.StatsDTO findJobSeekerDashboard(@Param("userId") String userId);
    
    // 최근 지원 내역
    List<DashboardDTO.ApplicationDTO> findRecentApplications(@Param("userId") String userId);
    
    // 기업 프로필
    CompanyProfileDTO findCompanyProfile(@Param("companyId") String companyId);
    
    // 채용 현황 통계
    RecruitmentStatsDTO findRecruitmentStats(@Param("companyId") String companyId);
    
    // 진행중인 공고
    List<JobPostDTO> findActivePosts(@Param("companyId") String companyId);
    
    // 추천 인재
    List<CandidateDTO> findRecommendedCandidates(@Param("companyId") String companyId);
    
    // 기업 평점 정보
    CompanyRatingDTO findCompanyRating(@Param("companyId") String companyId);
    
    // 맞춤 추천 공고
    List<JobPostDTO> findRecommendedPosts(@Param("userId") String userId);
    
    // 마감 임박 공고
    List<JobPostDTO> findDeadlinePosts(@Param("userId") String userId);
    
    // 인기 기술스택 TOP 3
    List<PopularSkillDTO> findPopularSkills();
    
}