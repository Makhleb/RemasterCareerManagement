package com.example.test.service.rim;

import com.example.test.dao.rim.ApplicationDao;
import com.example.test.dto.rim.mypage.ApplicationStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    
    private final ApplicationDao applicationDao;

    /**
     * 사용자의 지원 현황 통계와 목록을 조회
     */
    public ApplicationStatusDTO getApplicationStatus(String userId) {
        return ApplicationStatusDTO.builder()
                .stats(applicationDao.selectApplicationStats(userId))
                .applications(applicationDao.selectApplicationList(userId, null))
                .build();
    }

    /**
     * 최근 지원 내역만 조회 (limit 있음)
     */
    public ApplicationStatusDTO getRecentApplications(String userId, int limit) {
        return ApplicationStatusDTO.builder()
                .stats(applicationDao.selectApplicationStats(userId))
                .applications(applicationDao.selectApplicationList(userId, limit))
                .build();
    }

    /**
     * 기업 리뷰 등록
     */
    @Transactional
    public void createScore(String userId, String companyId, int score) {
        // 이미 평점이 있는지 확인
        if (applicationDao.existsScore(userId, companyId)) {
            throw new IllegalStateException("이미 평점을 등록하셨습니다.");
        }
        
        applicationDao.insertScore(userId, companyId, score);
    }

    /**
     * 평점 수정
     */
    @Transactional
    public void updateScore(String userId, String companyId, int score) {
        // 평점 존재 여부 확인
        if (!applicationDao.existsScore(userId, companyId)) {
            throw new IllegalStateException("등록된 평점이 없습니다.");
        }
        
        applicationDao.updateScore(userId, companyId, score);
    }
}