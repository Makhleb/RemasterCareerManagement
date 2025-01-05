package com.example.test.dao.rim;

import com.example.test.dto.rim.mypage.ApplicationStatusDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationDao {
    // 지원 현황 통계 조회
    ApplicationStatusDTO.ApplicationStats selectApplicationStats(@Param("userId") String userId);
    
    // 지원 내역 목록 조회
    List<ApplicationStatusDTO.ApplicationDetail> selectApplicationList(
        @Param("userId") String userId, 
        @Param("limit") Integer limit
    );
    
    // 평점 존재 여부 확인
    boolean existsScore(@Param("userId") String userId, @Param("companyId") String companyId);
    
    // 평점 등록
    void insertScore(
        @Param("userId") String userId, 
        @Param("companyId") String companyId,
        @Param("score") int score
    );
    
    // 평점 삭제
    void deleteScore(@Param("userId") String userId, @Param("companyId") String companyId);
    
    // 평점 수정
    void updateScore(@Param("userId") String userId, @Param("companyId") String companyId, @Param("score") int score);
} 