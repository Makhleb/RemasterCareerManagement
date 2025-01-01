package com.example.test.service.common;

import com.example.test.dto.response.FileResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Created on 2025-01-01 by 최기환
 */
@Service
public class FileService {

    @Value("${file.upload.path}")
    private String uploadPath;



    public String loadImage(String fileGubn, String refId) {

        FileResponseDto fileResponseDto = new FileResponseDto();

        Path dirPath = Paths.get(uploadPath)
                .resolve(fileGubn)
                .resolve(refId)
                .normalize();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*")) {
            Path filePath = stream.iterator().next();
            byte[] fileBytes = Files.readAllBytes(filePath);
            String encodedFile = Base64.getEncoder().encodeToString(fileBytes);

            return encodedFile;

        } catch (Exception e) {
            return null;
        }
    }
}
