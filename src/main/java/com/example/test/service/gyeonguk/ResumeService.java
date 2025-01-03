package com.example.test.service.gyeonguk;

import com.example.test.dto.*;
import com.example.test.dao.gyeonguk.ResumeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 2024-12-27 by 이경욱
 * Description: 이력서 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeDao resumeDao;

    /**
     * 이력서 제목 및 인적사항 저장
     *
     * @param resumeDTO 이력서 제목 및 인적사항 DTO
     */
    public int savePersonalInfo(ResumeDTO resumeDTO) {
        resumeDao.insertPersonalInfo(resumeDTO);
        return resumeDTO.getResumeNo();
    }

    /**
     * 활동 정보 저장
     *
     * @param activityDTO 활동 데이터 DTO
     */
    public void saveActivityInfo(ActivityDTO activityDTO) {
        resumeDao.insertActivityInfo(activityDTO);
    }

    /**
     * 학력 저장
     *
     * @param educationDTO 학력 정보 DTO
     */
    public void saveEducationInfo(EducationDTO educationDTO) {
        resumeDao.insertEducationInfo(educationDTO);
    }

    /**
     * 자격증 저장
     *
     * @param licenseDTO 자격증 정보 DTO
     */
    public void saveLicenseInfo(LicenseDTO licenseDTO) {
        resumeDao.insertLicenseInfo(licenseDTO);
    }

    /**
     * 병역 사항 저장
     *
     * @param militaryDTO 병역 사항 DTO
     */
    public void saveMilitaryInfo(MilitaryDTO militaryDTO) {
        resumeDao.insertMilitaryInfo(militaryDTO);
    }

    /**
     * 이력서 스킬 저장
     *
     * @param resumeSkillDTOList 이력서 스킬 DTO
     */
    public void saveSkills(List<ResumeSkillDTO> resumeSkillDTOList) {
        resumeDao.insertSkills(resumeSkillDTOList);
    }



    /**
     * 포트폴리오 저장
     *
     * @param portfolioDTO 포트폴리오 정보 DTO
     */
    public void savePortfolioInfo(PotfolioDTO portfolioDTO) {
        resumeDao.insertPortfolioInfo(portfolioDTO);
    }

    /**
     * 자기소개서 저장
     *
     * @param introDTO 자기소개서 정보 DTO
     */
    public void saveIntro(IntroduceDTO introDTO) {
        resumeDao.insertIntro(introDTO);
    }


    public ResumeDetailDTO getResumeDetail(int resumeNo) {
        ResumeDetailDTO detail = new ResumeDetailDTO();
        ResumeDTO resume = resumeDao.selectResumeByNo(resumeNo);
        if (resume == null) {
            return null; // 또는 예외 처리
        }
        detail.setResume(resume);
        System.out.println(resume.getResumeNo()+"............");
        detail.setActivities(resumeDao.selectActivitiesByResumeNo(resumeNo));
        System.out.println(detail.getActivities()+"............");
        detail.setEducations(resumeDao.selectEducationsByResumeNo(resumeNo));
        System.out.println(detail.getEducations()+"............");
        detail.setLicenses(resumeDao.selectLicensesByResumeNo(resumeNo));
        System.out.println(detail.getLicenses()+"............");
        detail.setSkills(resumeDao.selectSkillsByResumeNo(resumeNo));
        System.out.println(detail.getSkills()+"............");
        detail.setMilitary(resumeDao.selectMilitaryByResumeNo(resumeNo));
        System.out.println(detail.getMilitary()+"............");
        detail.setPortfolios(resumeDao.selectPortfoliosByResumeNo(resumeNo));
        System.out.println(detail.getPortfolios()+"............");
        detail.setIntro(resumeDao.selectIntroByResumeNo(resumeNo));
        System.out.println(detail.getIntro()+"............");
        return detail;
    }


}
