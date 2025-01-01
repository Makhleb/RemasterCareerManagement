package com.example.test.vo;

import com.example.test.dto.BenefitDTO;
import com.example.test.dto.JobPostSkillDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created on 2024-12-30 by 한상인
 */
@Data
public class JobPostDetailVo {

    private int jobPostNo; // 공고 인덱스
    private String companyId; // 기업 아이디
    private String title; // 공고 제목
    private Integer jobHistory; // 경력
    private Integer jobSalary; // 급여
    private Date startDate; // 공고 시작일
    private Date endDate; // 공고 마감일
    private String content; // 모집부분 및 상세내용
    private String method; // 접수기간 및 방법
    private String addNotice; // 추가 유의사항
    private String managerName; // 담당자명
    private String managerPhone; // 담당자 전화번호
    private String managerEmail; // 담당자 이메일
    private String postThumbnail; // 대표 이미지 썸네일
    private String postAddress; // 근무지주소
    private String postAddressDetail; // 근무지상세주소
    private String postZonecode; // 근무지우편번호
    private String postViewYn; // 공고 보기 여부
    private String workCode; // 직무 코드
    private String jobRankCode; // 직급/직책 코드
    private String workTypeCode; // 근무형태 코드
    private String educationCode; // 학력 코드

    // 밑에는 jobPostDto 에서 추가한 것입니다! - 상인
    private String companyName; // 기업명
    private List<JobPostSkillDTO> skillList; // 기술 스택 리스트
    private List<BenefitDTO> benefitList; // 복리후생 리스트
    private String educationName; // 학력 코드 -> 한글화
    private String workTypeName; //직무 코드 -> 한글화
    private int isScraped; // 공고 scrap 용
    private int likeCount; // 스크랩 수
}
