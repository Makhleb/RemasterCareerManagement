package com.example.test.controller.api.gihwan;

import com.example.test.dto.FileDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created on 2024-12-30 by 최기환
 */
@RestController
@RequestMapping("/api/common/file")
public class FileApiController {

    @PostMapping
    public boolean registFile(@RequestParam("file") MultipartFile file, @RequestBody FileDto fileDto) {
        //todo 서비스로 뺍시다.
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads" + "/" + fileDto.getFileGubn() + "/" + fileDto.getFileRefId();
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

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
