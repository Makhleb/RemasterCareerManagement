package com.example.test.dao.gyeonguk;

import com.example.test.dto.ResumeDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on 2024-12-27 by 이경욱
 * 이력서 관련 DAO 인터페이스
 */
@Mapper
public interface ResumeDao {

    /**
     * 이력서를 등록합니다.
     * @param resumeDTO 등록할 이력서 데이터
     */
    void insertResume(ResumeDTO resumeDTO);

    /**
     * 유저 정보를 조회합니다.
     * @param userId 조회할 유저의 ID
     * @return 유저 정보 DTO
     */
    ResumeDTO findUserById(String userId);
}
