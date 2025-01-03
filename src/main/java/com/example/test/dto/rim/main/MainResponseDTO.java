package com.example.test.dto.rim.main;

import lombok.Data;
import java.util.List;

@Data
public class MainResponseDTO {
    private CommonSectionDTO common;
    private UserTypeDTO userSection;  // 사용자 타입별 섹션
    private List<PopularSkillDTO> popularSkills;  // 추가
} 