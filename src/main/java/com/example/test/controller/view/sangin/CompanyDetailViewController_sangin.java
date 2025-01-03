package com.example.test.controller.view.sangin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created on 2025-01-03 by 한상인
 */
@Controller
@RequestMapping("/view/users/company")
public class CompanyDetailViewController_sangin {

    @GetMapping("/detail/{companyId}")
    public String detail(@PathVariable String companyId, Model model) {
        System.out.println("detail/companyId = " + companyId);
        model.addAttribute("companyId", companyId);
        return "/sangin/company-detail";
    }
}
