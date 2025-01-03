package com.example.test.controller.api.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//todo 패키지 생성용 컨트롤러입니다. 작업시 이 파일은 삭제해주세요!!!
@Controller
public class InViewController {
//    @GetMapping("test")
    public String test() {
        return "sangin/test";
    }
}

