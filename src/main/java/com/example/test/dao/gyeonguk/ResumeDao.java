package com.example.test.dao.gyeonguk;

import com.example.test.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created on 2024-12-27 by 이경욱
 * Description: 이력서 데이터 처리용 DAO 인터페이스
 */
@Mapper
public interface ResumeDao {


    /**
     * 특정 사용자 ID로 사용자 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 유저 정보
     */
    UserDTO selectUserInfoById(String userId);


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
     * 이력서스킬 저장
     *
     * @param resumeSkillDTOList 이력서스킬 정보 DTO
     */
    void insertSkills(@Param("skills")List<ResumeSkillDTO> resumeSkillDTOList); // 추가된 메소드


    /**
     * 병역 사항 저장
     *
     * @param militaryDTO 병역 사항 DTO
     */
    void insertMilitaryInfo(MilitaryDTO militaryDTO);

    /**
     * 포트폴리오 저장
     *
     * @param potfolioDTO 포트폴리오 정보 DTO
     */
    void insertPotfolioInfo(PotfolioDTO potfolioDTO);

    /**
     * 자기소개서 저장
     *
     * @param introDTO 자기소개서 정보 DTO
     */
    void insertIntro(IntroduceDTO introDTO);

    /**
     * 특정 사용자 ID로 모든 이력서를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 이력서 리스트
     */
    List<ResumeDTO> selectResumesByUserId(String userId);

    /**
     * 기존 대표 이력서를 초기화합니다.
     *
     * @param userId 사용자 ID
     */
    void clearRepresentativeResume(@Param("userId") String userId);

    /**
     * 특정 사용자의 대표 이력서를 조회합니다.
     * @param userId 사용자 ID
     * @return 대표 이력서
     */
    ResumeDTO selectRepresentativeResume(String userId);

    /**
     * 특정 이력서를 대표이력서로 설정합니다.
     *
     * @param resumeNo 이력서 ID
     * @param userId   사용자 ID
     */
    void updateRepresentativeResume(@Param("resumeNo") int resumeNo, @Param("userId") String userId);

    // 이력서 기본 정보 조회
    ResumeDTO selectResumeByNo(int resumeNo);

    // 이력서 섹션별 정보 조회
    ActivityDTO selectActivitiesByResumeNo(int resumeNo);
    EducationDTO selectEducationsByResumeNo(int resumeNo);
    LicenseDTO selectLicensesByResumeNo(int resumeNo);
    List<ResumeSkillDTO> selectSkillsByResumeNo(int resumeNo);
    MilitaryDTO selectMilitaryByResumeNo(int resumeNo);
    PotfolioDTO selectPotfoliosByResumeNo(int resumeNo);
    IntroduceDTO selectIntroByResumeNo(int resumeNo);

    /**
     * 해당 이력서가 특정 회사에 지원되었는지 확인
     * @param resumeNo 이력서 번호
     * @param companyId 회사 ID
     * @return 지원 여부
     */
    boolean isAppliedToCompany(@Param("resumeNo") int resumeNo, @Param("companyId") String companyId);

}
