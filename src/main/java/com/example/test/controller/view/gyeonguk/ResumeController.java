package com.example.test.controller.view.gyeonguk;

import com.example.test.dto.ResumeDTO;
import com.example.test.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created on 2024-12-27 by 이경욱
 * 이력서 등록 화면 컨트롤러
 */
@Controller
public class ResumeController {

    /**
     * 이력서 등록 화면 반환
     * @return 이력서 등록 화면 뷰 이름
     */
    @GetMapping("/resume/register")
    public String showResumeForm() {


        return "/gyeonguk/resumeRegist"; // 타임리프 HTML 파일 이름
    }
}
