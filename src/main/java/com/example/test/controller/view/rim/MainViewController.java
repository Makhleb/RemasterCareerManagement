package com.example.test.controller.view.rim;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class MainViewController {

    @GetMapping
    public String index() {
        return "rim/index";  // templates/rim/index.html
    }
}