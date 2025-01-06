package com.example.test.controller.view.gyeonguk;

import com.example.test.dto.ResumeDTO;
import com.example.test.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created on 2024-12-27 by 이경욱
 * 이력서 등록 화면 컨트롤러
 */
@Controller
@RequestMapping("/resume")
public class ResumeController {


    /**
     * 이력서 등록 화면 반환
     * @return 이력서 등록 화면 뷰 이름
     */
    @GetMapping("/register")
    public String showResumeForm() {


        return "/gyeonguk/resumeRegist"; // 타임리프 HTML 파일 이름
    }

    /**
     * 이력서 리스트 화면 반환
     * @return 이력서 리스트 화면 뷰 이름
     */
    @GetMapping("/list")
    public String showResumeList() {


        return "/gyeonguk/resumeList"; // 타임리프 HTML 파일 이름
    }

    /**
     * 이력서 상세보기 화면 반환
     * @param resumeNo 이력서 번호
     * @return 이력서 상세보기 화면 뷰 이름
     */
    @GetMapping("/detail/{resumeNo}")
    public String showResumeDetail(@PathVariable int resumeNo) {
        return "gyeonguk/resumeDetail"; // src/main/resources/templates/gyeonguk/resumeDetail.html
    }




}
