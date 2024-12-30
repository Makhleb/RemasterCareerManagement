package com.example.test.exception;

import com.example.test.dto.common.ApiResponse;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on 2024-12-30 by 구경림
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 인증 예외 (401)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<String> handleUnauthorized(UnauthorizedException e) {
        return ApiResponse.error(401, e.getMessage());
    }

    // 권한 예외 (403)
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<String> handleForbidden(ForbiddenException e) {
        return ApiResponse.error(403, e.getMessage());
    }

    // 리소스 없음 (404)
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<String> handleNotFound(NotFoundException e) {
        return ApiResponse.error(404, e.getMessage());
    }

    // 잘못된 요청 (400)
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleBadRequest(BadRequestException e) {
        return ApiResponse.error(400, "BUSINESS_ERROR", e.getMessage());
    }

    // 서버 오류 (500)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleException(Exception e) {
        log.error("시스템 에러 발생", e);
        return ApiResponse.error(500, "서버 오류가 발생했습니다");
    }

    // 유효성 검사 예외 (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage
            ));

        return ApiResponse.error(400, "VALIDATION_ERROR", errors);
    }


    /**
     * @Validation 예외 프론트 유효성검사 예시
     * try {
     *         await axios.post('/api/users', userData);
     *     } catch (error) {
     *         if (error.response.status === 400) {
     *             // 응답예시 -> error.response.data.body = { "email": "이메일 형식이 잘못됨", "name": "이름은 필수입니다" }
     *         const errors = error.response.data.body;
     *             Object.entries(errors).forEach(([field, message]) => {
     *                 // 각 필드에 에러 메시지 표시
     *                 showFieldError(field, message);
     *             });
     *         }
     *     }
     */
} 