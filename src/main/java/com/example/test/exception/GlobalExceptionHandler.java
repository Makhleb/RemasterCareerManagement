package com.example.test.exception;

import com.example.test.dto.common.ApiResponse;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on 2024-12-30 by 구경림
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 커스텀 비즈니스 예외 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        log.error("비즈니스 예외 발생: {}", e.getMessage());
        return ResponseEntity
            .status(e.getStatus())
            .body(ApiResponse.error(e.getCode(), e.getMessage()));
    }

    // 인증 예외 처리
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthException(AuthException e) {
        log.error("인증 예외 발생: {}", e.getMessage());
        return ResponseEntity
            .status(e.getStatus())
            .body(ApiResponse.error(e.getCode(), e.getMessage()));
    }

    // 유효성 검사 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage
            ));

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error("VALIDATION_ERROR", "입력값이 올바르지 않습니다"));
    }

    // 기타 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("시스템 에러 발생", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("SYSTEM_ERROR", "서버 오류가 발생했습니다"));
    }
} 