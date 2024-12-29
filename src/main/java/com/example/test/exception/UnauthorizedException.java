package com.example.test.exception;

/**
 * Created on 2024-12-30 by 구경림
 */
/**
 * 권한이 없는 경우 발생하는 예외
 * 예: 토큰이 없거나, 유효하지 않은 토큰, 접근 권한 부족 등
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
} 