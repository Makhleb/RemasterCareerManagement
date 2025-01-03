package com.example.test.exception;
/**
 * Created on 2024-1-1 by 구경림
 */
public class BaseException extends RuntimeException {
    private final String code;
    private final int status;

    public BaseException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
} 