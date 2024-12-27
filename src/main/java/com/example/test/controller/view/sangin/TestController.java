package com.example.test.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @GetMapping("/template")
    public String test() {
        System.out.println("test...");
        return "sangin/template";
    }
    @GetMapping("/test2")
    public String test2() {
        System.out.println("test2...");
        return "layout_dir/layout/main_layout";
    }


}
