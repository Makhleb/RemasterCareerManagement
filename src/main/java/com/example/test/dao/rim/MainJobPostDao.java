package com.example.test.dao.rim;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MainJobPostDao {
    
    // 인기 기술스택별 채용공고 조회
    List<Map<String, Object>> findPopularPostsBySkills();
    
    // 스크랩순 채용공고 조회
    List<Map<String, Object>> findPostsByScrapCount();
    
    // 조회수순 채용공고 조회
    List<Map<String, Object>> findPostsByViewCount();
} 