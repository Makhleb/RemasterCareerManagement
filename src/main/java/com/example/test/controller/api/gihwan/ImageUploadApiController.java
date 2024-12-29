package com.example.test.controller.api.gihwan;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created on 2024-12-29 by 최기환
 */
@RestController
@RequestMapping("/api/companies/image")
public class ImageUploadApiController {
    @PostMapping
    public boolean uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/portfolio" + "/" + user.getUserId() + "/" + resumeNo;
            File directory = new File(uploadDir);

            if (!directory.exists()) {
                boolean isCreated = directory.mkdirs();  // 디렉토리 생성 여부 확인
                if (isCreated) {
                    System.out.println("업로드 디렉터리가 생성되었습니다: " + uploadDir);
                } else {
                    throw new IOException("디렉터리 생성 실패");
                }
            }

                String filePath = uploadDir + File.separator + file.getOriginalFilename();
                file.transferTo(new File(filePath));

            return ResponseEntity.ok("모든 파일 업로드 성공");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 업로드 실패: " + e.getMessage());
        }
        return true;
    }
}
