package com.example.test.controller.api.gyeonguk;

import com.example.test.dao.gyeonguk.ResumeDao;
import com.example.test.dto.*;
import com.example.test.security.rim.RequireToken;
import com.example.test.security.rim.SecurityUtil;
import com.example.test.service.gihwan.JobPostService;
import com.example.test.service.gyeonguk.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final ResumeDao resumeDao;
    private final SecurityUtil securityUtil;

    /**
     * 이력서 제목 및 인적사항 저장
     *
     * @param resumeDTO 이력서 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/personal")
    public int savePersonalInfo(@RequestBody ResumeDTO resumeDTO) {
        String userId = securityUtil.getCurrentUserId();
        resumeDTO.setUserId(userId);
        int resumeNo =resumeService.savePersonalInfo(resumeDTO);
        return resumeNo;
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserDTO> getUserInfo(@RequestParam String userId) {
        UserDTO userInfo = resumeService.getUserInfo(userId); // 사용자 정보 조회
        return ResponseEntity.ok(userInfo);
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
     * @param potfolioDTO 포트폴리오 데이터 DTO
     * @return 저장 결과 메시지
     */
    @PostMapping("/potfolio")
    public String savePortfolioInfo(@RequestBody PotfolioDTO potfolioDTO) {
        resumeService.savePotfolioInfo(potfolioDTO);
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

    /**
     * 특정 사용자의 이력서를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 이력서 리스트
     */
    @GetMapping("/list/{userId}")
    public List<ResumeDTO> getResumesByUserId(@PathVariable String userId) {
        return resumeDao.selectResumesByUserId(userId);
    }

    /**
     * 특정 사용자의 대표 이력서를 조회합니다.
     * @param userId 사용자 ID
     * @return 대표 이력서
     */
    @GetMapping("/representative/{userId}")
    @RequireToken(roles = {"ROLE_USER"})
    public ResumeDTO getRepresentativeResume(@PathVariable String userId) {
        return resumeDao.selectRepresentativeResume(userId);
    }





    /**
     * 특정 이력서를 대표이력서로 설정합니다.
     *
     * @param resumeDTO 이력서
     * @return 성공 메시지
     */
    @PutMapping("/representative")
    @RequireToken(roles = {"ROLE_USER"})
    public String setRepresentativeResume(@RequestBody ResumeDTO resumeDTO) {
        resumeDao.clearRepresentativeResume(resumeDTO.getUserId());
        resumeDao.updateRepresentativeResume(resumeDTO.getResumeNo(), resumeDTO.getUserId());
        return "대표 이력서가 설정되었습니다.";
    }

    /**
     * 특정 이력서의 상세 정보를 조회합니다.
     *
     * @param resumeNo 이력서 번호
     * @return 이력서 상세 정보
     */
    @GetMapping("/detail/{resumeNo}")
    @RequireToken(roles = {"ROLE_USER", "ROLE_COMPANY", "ROLE_ADMIN"}, checkResume = true)
    public ResponseEntity<ResumeDetailDTO> getResumeDetail(@PathVariable int resumeNo) {
        String userId = securityUtil.getCurrentUserId();
        ResumeDetailDTO detail = resumeService.getResumeDetail(resumeNo);
        if (detail != null) {
            return ResponseEntity.ok(detail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
