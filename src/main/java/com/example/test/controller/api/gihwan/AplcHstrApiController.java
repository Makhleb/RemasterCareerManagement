package com.example.test.controller.api.gihwan;

import com.example.test.dao.gihwan.GiAplcDao;
import com.example.test.dto.AplcHstrDTO;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2025-01-02 by 최기환
 */
@RestController
@RequestMapping("/api/companies/aplc")
public class AplcHstrApiController {

    private final GiAplcDao giAplcDao;

    public AplcHstrApiController(GiAplcDao giAplcDao) {
        this.giAplcDao = giAplcDao;
    }

    @PutMapping
    public boolean alpcUpdate(@RequestBody AplcHstrDTO aplcHstrDTO) {
        return giAplcDao.aplcUpadte(aplcHstrDTO);
    }
}
