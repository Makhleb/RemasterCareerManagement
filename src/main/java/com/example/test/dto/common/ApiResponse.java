package com.example.test.dto.common;

import lombok.Getter;
import java.time.LocalDateTime;

/**
 * Created on 2024-12-30 by 구경림
 */
@Getter
public class ApiResponse<T> {
    private final int status;      // HTTP 상태 코드
    private final String state;    // SUCCESS or ERROR
    private final String type;     // VALIDATION_ERROR or BUSINESS_ERROR (에러 구분용)
    private final T body;
    private final LocalDateTime timestamp;

    private ApiResponse(int status, String state, String type, T body) {
        this.status = status;
        this.state = state;
        this.type = type;
        this.body = body;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "SUCCESS", null, data);
    }

    public static <T> ApiResponse<T> error(int status, String type, T data) {
        return new ApiResponse<>(status, "ERROR", type, data);
    }
    public static <T> ApiResponse<T> error(int status, T data) {
        return new ApiResponse<>(status, "ERROR", null, data);
    }
}