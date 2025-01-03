package com.example.test.controller.api.gihwan;

import com.example.test.dao.gihwan.FileDao;
import com.example.test.dto.FileDto;
import com.example.test.service.common.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created on 2024-12-30 by 최기환
 */
@RestController
@RequestMapping("/api/common/file")
public class FileApiController {

    private final FileService fileService;
    private final FileDao fileDao;

    public FileApiController(FileService fileService, FileDao fileDao) {
        this.fileService = fileService;
        this.fileDao = fileDao;
    }

    /**
     * 파일 저장
     */
    @PostMapping
    public boolean registFile(@RequestPart("file") MultipartFile file, @RequestPart FileDto fileDto) {
        return fileService.saveImage(file, fileDto);
    }

    @PutMapping
    public boolean updateFile(@RequestPart("file") MultipartFile file, @RequestPart FileDto fileDto) throws IOException {
        System.out.println(fileDto.toString() + "받은 파일 dto...............");
        if(fileDao.selectFile(fileDto) != 0){
            return fileService.updateImage(file, fileDto);
        } else {
            return fileService.saveImage(file, fileDto);
        }
    }

    @DeleteMapping
    public boolean deleteFile(@RequestParam("gubn") String fileGubn, @RequestParam("id") String refId) throws IOException {
        return fileService.deleteImage(fileGubn,refId) != 0;
    }
}
