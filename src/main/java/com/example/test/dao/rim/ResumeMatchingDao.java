package com.example.test.dao.rim;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface ResumeMatchingDao {
    
    // 사용자의 이력서 목록 조회
    List<Map<String, Object>> findCompactResumes(@Param("userId") String userId);
    
    // 특정 이력서와 매칭되는 채용공고 목록 조회
    List<Map<String, Object>> findMatchingJobPosts(@Param("resumeNo") int resumeNo);
} 