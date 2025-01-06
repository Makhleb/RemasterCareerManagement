package com.example.test.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object error = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        model.addAttribute("status", status != null ? status : 500);
        model.addAttribute("error", getErrorTitle((Integer) status));
        model.addAttribute("message", getMessage((Integer) status, message));

        return "error/error";
    }

    private String getErrorTitle(Integer status) {
        if (status == null) return "Server Error";
        
        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Page Not Found";
            case 500 -> "Internal Server Error";
            default -> "Error";
        };
    }

    private String getMessage(Integer status, Object message) {
        if (message != null) return message.toString();
        
        return switch (status) {
            case 400 -> "잘못된 요청입니다.";
            case 401 -> "로그인이 필요한 서비스입니다.";
            case 403 -> "접근 권한이 없습니다.";
            case 404 -> "요청하신 페이지를 찾을 수 없습니다.";
            case 500 -> "서버 오류가 발생했습니다.";
            default -> "알 수 없는 오류가 발생했습니다.";
        };
    }
} 