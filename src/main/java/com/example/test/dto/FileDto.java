package com.example.test.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created on 2024-12-30 by 최기환
 */
@Data
public class FileDto {
    private Long fileId;
    private String fileGubn;
    private String fileRefId;
    private String fileName;
    private String fileExt;
    private String instId;
    private LocalDateTime instDt;
    private String updtId;
    private LocalDateTime updtDt;
}
