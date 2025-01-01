package com.example.test.dto.common;

import lombok.Getter;
import java.time.LocalDateTime;

/**
 * Created on 2024-12-30 by 구경림
 * API 응답의 표준 포맷을 정의하는 클래스
 */
@Getter
public class ApiResponse<T> {
    private final String status;    // SUCCESS or ERROR
    private final String code;      // 에러 코드 (성공시 null)
    private final String message;   // 에러 메시지 (성공시 null)
    private final T data;          // 실제 데이터 (에러시 null)
    private final LocalDateTime timestamp;

    private ApiResponse(String status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", null, null, data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("SUCCESS", null, message, data);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>("ERROR", code, message, null);
    }
}