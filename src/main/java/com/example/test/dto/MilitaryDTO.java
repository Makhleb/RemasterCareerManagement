package com.example.test.dto;

import lombok.Data;

@Data
public class MilitaryDTO {
    private int resumeNo; // 이력서 인덱스
    private Date enlistDate; // 입대년월
    private Date releaseDate; // 전역년월
    private String militaryType; // 복무유형
}
