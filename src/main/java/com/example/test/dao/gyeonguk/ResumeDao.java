package com.example.test.dao.gyeonguk;

import com.example.test.dto.*;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on 2024-12-27 by 이경욱
 * Description: 이력서 데이터 처리용 DAO 인터페이스
 */
@Mapper
public interface ResumeDao {

    /**
     * 이력서 제목 및 인적사항 저장
     *
     * @param resumeDTO 이력서 제목 및 인적사항 DTO
     */
    void insertPersonalInfo(ResumeDTO resumeDTO);

    /**
     * 활동 정보 저장
     *
     * @param activityDTO 활동 데이터 DTO
     */
    void insertActivityInfo(ActivityDTO activityDTO);

    /**
     * 학력 저장
     *
     * @param educationDTO 학력 정보 DTO
     */
    void insertEducationInfo(EducationDTO educationDTO);

    /**
     * 자격증 저장
     *
     * @param licenseDTO 자격증 정보 DTO
     */
    void insertLicenseInfo(LicenseDTO licenseDTO);

    /**
     * 병역 사항 저장
     *
     * @param militaryDTO 병역 사항 DTO
     */
    void insertMilitaryInfo(MilitaryDTO militaryDTO);

    /**
     * 포트폴리오 저장
     *
     * @param portfolioDTO 포트폴리오 정보 DTO
     */
    void insertPortfolioInfo(PotfolioDTO portfolioDTO);

    /**
     * 자기소개서 저장
     *
     * @param introDTO 자기소개서 정보 DTO
     */
    void insertIntro(IntroduceDTO introDTO);

    /**
     * 유저 정보 조회
     *
     * @param userId 조회할 유저 ID
     * @return 유저 정보 DTO
     */
    ResumeDTO selectUserInfo(String userId);
}
