package com.example.test.controller.api.gihwan;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created on 2024-12-27 by 최기환
 */
@RestController
@RequestMapping("/api/companies")
public class JobPostApiController {
//    @PostMapping
//    public ResponseEntity<?> jobPostRegist(){
//
//    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
