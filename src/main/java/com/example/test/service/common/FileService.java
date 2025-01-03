package com.example.test.service.common;

import com.example.test.dao.gihwan.FileDao;
import com.example.test.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;
import java.util.Objects;

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

            return "data:image/png;base64,"+ Base64.getEncoder().encodeToString(fileBytes);

        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateImage(MultipartFile file, FileDto fileDto) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 잘못되었습니다.");
        }

        Path dirPath = Paths.get(System.getProperty("user.dir"), uploadPath, fileDto.getFileGubn(), fileDto.getFileRefId());

        if (Files.exists(dirPath)) {
            Files.list(dirPath).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException ignored) {}
            });

            Path filePath = dirPath.resolve(file.getOriginalFilename());
            file.transferTo(filePath.toFile());
        }
        return fileDao.updateFile(fileDto) != 0;
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

    public int deleteImage(String fileGubn, String refId) throws IOException {
        Path dirPath = Paths.get(uploadPath)
                .resolve(fileGubn)
                .resolve(refId)
                .normalize();

        if (Files.exists(dirPath)) {
            // 폴더 내 파일 삭제
            Files.list(dirPath).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    System.out.println("파일 삭제 실패: " + dirPath + "/" + path);
                }
                System.out.println("파일 삭제 성공");
            });
            // 폴더 자체 삭제
            Files.delete(dirPath);
        } else {
            System.out.println("폴더가 존재하지 않습니다: " + dirPath);
        }
        return fileDao.deleteFile(fileGubn, refId);
    }
}
