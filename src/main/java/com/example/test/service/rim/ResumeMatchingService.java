package com.example.test.service.rim;

import com.example.test.dao.rim.ResumeMatchingDao;
import com.example.test.security.rim.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeMatchingService {
    
    private final ResumeMatchingDao resumeMatchingDao;
    private final SecurityUtil securityUtil;

    /**
     * 현재 로그인한 사용자의 이력서 목록 조회
     */
    public List<Map<String, Object>> getCompactResumes() {
        String userId = securityUtil.getCurrentUserId();
        return resumeMatchingDao.findCompactResumes(userId);
    }

    /**
     * 특정 이력서와 매칭되는 채용공고 목록 조회
     */
    public List<Map<String, Object>> getMatchingJobPosts(int resumeNo) {
        return resumeMatchingDao.findMatchingJobPosts(resumeNo);
    }
} 