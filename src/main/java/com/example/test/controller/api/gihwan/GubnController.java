package com.example.test.controller.api.gihwan;

import com.example.test.dao.gihwan.GubnDao;
import com.example.test.dto.GubnDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created on 2024-12-29 by 최기환
 * gubn 조회용 컨트롤러
 * todo 이건 경로를 어떻게 정할까요?
 */
@RestController
@RequestMapping("/api/companies/gubn")
public class GubnController {

    private final GubnDao gubnDao;

    public GubnController(GubnDao gubnDao) {
        this.gubnDao = gubnDao;
    }

    @GetMapping
    public List<GubnDTO> getGubn(@RequestParam List<String> groupCodes){
        return gubnDao.getGubn(groupCodes);
    }
}
