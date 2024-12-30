package com.example.test.controller.api.gyeonguk;

import com.example.test.dto.*;
import com.example.test.service.gyeonguk.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 2024-12-27 by 이경욱
 * Description: 이력서 등록 및 섹션별 저장을 위한 REST 컨트롤러
 */
@RestController
@RequestMapping("/api/users/resume")
@RequiredArgsConstructor
public class ResumeRegistApiController {

    private final ResumeService resumeService;

    /**
     * 이력서 제목 및 인적사항 저장
     *
     * @param resumeDTO 이력서 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/personal")
    public int savePersonalInfo(@RequestBody ResumeDTO resumeDTO) {
        resumeDTO.setUserId("test1");
        int resumeNo=resumeService.savePersonalInfo(resumeDTO);
        return resumeNo;
    }

    /**
     * 활동 정보 저장
     *
     * @param activityDTO 활동 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/activity")
    public String saveActivityInfo(@RequestBody ActivityDTO activityDTO) {
        resumeService.saveActivityInfo(activityDTO);
        return "활동 정보가 저장되었습니다.";
    }

    /**
     * 학력 저장
     *
     * @param educationDTO 학력 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/education")
    public String saveEducationInfo(@RequestBody EducationDTO educationDTO) {
        System.out.println(educationDTO + ".............");
        resumeService.saveEducationInfo(educationDTO);
        return "학력이 저장되었습니다.";
    }

    /**
     * 자격증 저장
     *
     * @param licenseDTO 자격증 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/license")
    public String saveLicenseInfo(@RequestBody LicenseDTO licenseDTO) {
        resumeService.saveLicenseInfo(licenseDTO);
        return "자격증이 저장되었습니다.";
    }
    /**
     * 스킬 저장
     *
     * @param resumeSkillDTOList 스킬 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/skills")
    public ResponseEntity<String> saveSkills(@RequestBody List<ResumeSkillDTO> resumeSkillDTOList ) {
        resumeService.saveSkills(resumeSkillDTOList);
        return ResponseEntity.ok("스킬이 저장되었습니다.");
    }



    /**
     * 병역사항 저장
     *
     * @param militaryDTO 병역 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/military")
    public String saveMilitaryInfo(@RequestBody MilitaryDTO militaryDTO) {
        resumeService.saveMilitaryInfo(militaryDTO);
        return "병역사항이 저장되었습니다.";
    }

    /**
     * 포트폴리오 저장
     *
     * @param portfolioDTO 포트폴리오 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/portfolio")
    public String savePortfolioInfo(@RequestBody PotfolioDTO portfolioDTO) {
        resumeService.savePortfolioInfo(portfolioDTO);
        return "포트폴리오가 저장되었습니다.";
    }

    /**
     * 자기소개서 저장
     *
     * @param introDTO 자기소개서 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/intro")
    public String saveIntro(@RequestBody IntroduceDTO introDTO) {
        resumeService.saveIntro(introDTO);
        return "자기소개서가 저장되었습니다.";
    }
}
