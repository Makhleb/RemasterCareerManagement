package com.example.test.exception;
/**
 * Created on 2024-1-1 by 구경림
 */
public class AuthException extends BaseException {
    // 인증 관련 에러 코드
    public static final String LOGIN_FAILED = "AUTH_LOGIN_FAILED";
    public static final String INVALID_TOKEN = "AUTH_INVALID_TOKEN";
    public static final String TOKEN_EXPIRED = "AUTH_TOKEN_EXPIRED";
    public static final String UNAUTHORIZED = "AUTH_UNAUTHORIZED";
    public static final String AUTH_FAILED = "AUTH_FAILED";
    public static final String FORBIDDEN = "AUTH_FORBIDDEN";

    public AuthException(String code, String message, int status) {
        super(code, message, status);
    }

    public AuthException(String code, String message) {
        super(code, message, 401);
    }

    // 팩토리 메서드들
    public static AuthException loginFailed() {
        return new AuthException(LOGIN_FAILED, "아이디 또는 비밀번호가 일치하지 않습니다");
    }

    public static AuthException invalidToken() {
        return new AuthException(INVALID_TOKEN, "유효하지 않은 토큰입니다");
    }

    public static AuthException tokenExpired() {
        return new AuthException(TOKEN_EXPIRED, "토큰이 만료되었습니다");
    }

    public static AuthException unauthorized() {
        return new AuthException(UNAUTHORIZED, "인증이 필요합니다");
    }

    public static AuthException authenticationFailed(String message) {
        return new AuthException(AUTH_FAILED, message);
    }

    public static AuthException forbidden() {
        return new AuthException(FORBIDDEN, "접근 권한이 없습니다", 403);
    }

    public static AuthException forbidden(String message) {
        return new AuthException(FORBIDDEN, message, 403);
    }

    public static AuthException invalidUserType() {
        return new AuthException("AUTH_INVALID_USER_TYPE", "유효하지 않은 사용자 타입입니다.");
    }
} 