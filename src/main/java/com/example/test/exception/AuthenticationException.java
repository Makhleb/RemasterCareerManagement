package com.example.test.exception;

/**
 * Created on 2024-12-30 by 구경림
 */
/**
 * 인증 처리 중 발생하는 예외
 * 예: 토큰 파싱 실패, 인증 정보 설정 실패 등
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}