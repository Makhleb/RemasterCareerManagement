package com.example.test.service.common;

import com.example.test.dao.gihwan.FileDao;
import com.example.test.dto.FileDto;
import com.example.test.dto.response.FileResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    private final FileDao fileDao;
    @Value("${file.upload.path}")
    private String uploadPath;

    public FileService(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    public String loadImage(String fileGubn, String refId) {

        Path dirPath = Paths.get(uploadPath)
                .resolve(fileGubn)
                .resolve(refId)
                .normalize();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*")) {
            Path filePath = stream.iterator().next();
            byte[] fileBytes = Files.readAllBytes(filePath);

            return Base64.getEncoder().encodeToString(fileBytes);

        } catch (Exception e) {
            return null;
        }
    }

    public boolean saveImage(MultipartFile file, FileDto fileDto) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads" + "/" + fileDto.getFileGubn() + "/" + fileDto.getFileRefId();
            File directory = new File(uploadDir);

            //디렉토리 없으면 추가
            if (!directory.exists()) {
                boolean isCreated = directory.mkdirs();  // 디렉토리 생성 여부 확인
                if (isCreated) {
                    System.out.println("업로드 디렉터리가 생성되었습니다: " + uploadDir);
                } else {
                    throw new IOException("디렉터리 생성 실패");
                }
            }

            //uploads에 추가
            String filePath = uploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            //파일 업로드 후 db에 정보 등록
            int result = fileDao.insertFile(fileDto);
            return result != 0;
        } catch (IOException e) {
            return false;
        }
    }
}
