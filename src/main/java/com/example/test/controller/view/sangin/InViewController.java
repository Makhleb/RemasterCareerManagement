package com.example.test.controller.view.sangin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created on 2024-12-27 by 한상인
 */
@RequestMapping("/sangin")
public class InViewController {
    @GetMapping("/test")
    public String test() {
        return "sangin/test";
    }
}
