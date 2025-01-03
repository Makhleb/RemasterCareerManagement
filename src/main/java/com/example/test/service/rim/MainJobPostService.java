package com.example.test.service.rim;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.test.dao.rim.MainJobPostDao;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainJobPostService {
    
    private final MainJobPostDao jobPostDao;

    // 인기 기술스택별 채용공고 조회
    public List<Map<String, Object>> findPopularPostsBySkills() {
        return jobPostDao.findPopularPostsBySkills();
    }

    // 주목받는 채용공고 조회 (스크랩/조회수 기준)
    public List<Map<String, Object>> findTrendingPosts(String sortBy) {
        if ("view".equals(sortBy)) {
            return jobPostDao.findPostsByViewCount();
        }
        return jobPostDao.findPostsByScrapCount();
    }
} 