package com.example.test.dto.common;

import lombok.Getter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

/**
 * Created on 2024-12-30 by 구경림
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private ApiStatus status;
    private T body;
    private LocalDateTime timestamp;

    public ApiResponse(ApiStatus status, T body) {
        this.status = status;
        this.body = body;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiStatus.SUCCESS, data);
    }

    public static <T> ApiResponse<T> error(T error) {
        return new ApiResponse<>(ApiStatus.ERROR, error);
    }

    public enum ApiStatus {
        SUCCESS, ERROR
    }
}