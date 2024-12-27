package com.example.test.service.gyeonguk;

import org.springframework.stereotype.Service;
import com.example.test.dto.ResumeDTO;
import com.example.test.dao.gyeonguk.ResumeDao;
import lombok.RequiredArgsConstructor;

/**
 * Created on 2024-12-27 by 이경욱
 * Description: 이력서 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeDao resumeDao;

    /**
     * 이력서를 등록하는 메서드
     * @param resumeDTO 등록할 이력서 데이터
     * @return 등록 성공 여부 (boolean)
     */
    public boolean registerResume(ResumeDTO resumeDTO) {
        resumeDao.insertResume(resumeDTO);
        return true;
    }

    /**
     * 유저 정보를 조회하는 메서드
     * @param userId 유저 ID
     * @return 유저 정보
     */
    public ResumeDTO getUserInfo(String userId) {
        return resumeDao.findUserById(userId);
    }
}
