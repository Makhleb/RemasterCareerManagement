package com.example.test.controller.api.rim;

import com.example.test.dto.rim.main.MainDTO;
import com.example.test.service.rim.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    // 메인 페이지 전체 데이터 조회
    @GetMapping("/data")
    public MainDTO getMainPageData() {
        return mainService.getMainPageData();
    }
} 