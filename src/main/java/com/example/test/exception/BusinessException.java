package com.example.test.exception;
/**
 * Created on 2024-1-1 by 구경림
 */
public class BusinessException extends BaseException {
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String FORBIDDEN = "FORBIDDEN";

    public BusinessException(String code, String message, int status) {
        super(code, message, status);
    }

    public static BusinessException badRequest(String message) {
        return new BusinessException(BAD_REQUEST, message, 400);
    }

    public static BusinessException notFound(String message) {
        return new BusinessException(NOT_FOUND, message, 404);
    }

    public static BusinessException forbidden(String message) {
        return new BusinessException(FORBIDDEN, message, 403);
    }
} 