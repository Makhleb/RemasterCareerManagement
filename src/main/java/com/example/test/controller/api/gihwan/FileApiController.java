package com.example.test.controller.api.gihwan;

import com.example.test.dao.gihwan.FileDao;
import com.example.test.dto.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2024-12-30 by 최기환
 */
@RestController
@RequestMapping("/api/common/file")
public class FileApiController {

    @Value("${file.upload.path}")
    private String uploadPath;


    private final FileDao fileDao;

    public FileApiController(FileDao fileDao) {
        this.fileDao = fileDao;
    }

    @PostMapping
    public boolean registFile(@RequestPart("file") MultipartFile file, @RequestPart FileDto fileDto) {
        //todo 서비스로 뺍시다. 그리고 이미 파일에 내용이 있다면 삭제? 추가?
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

    @PostMapping("/list")
    public Map<String, String> loadImage(@RequestBody List<FileDto> fileDtos) throws IOException {
        Map<String, String> files = new HashMap<>();

        for (FileDto fileDto : fileDtos) {
            Path filePath = Paths.get(uploadPath)
                    .resolve(fileDto.getFileGubn())
                    .resolve(fileDto.getFileRefId())
                    .resolve(fileDto.getFileName())
                    .normalize();


            File file = filePath.toFile();
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String encodedFile = Base64.getEncoder().encodeToString(fileBytes);

            files.put(fileDto.getFileRefId(), encodedFile);
        }

        return files;
    }
}
