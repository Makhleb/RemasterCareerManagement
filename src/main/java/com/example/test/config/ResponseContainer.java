package com.example.test.config;

import com.example.test.dto.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.StringHttpMessageConverter;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ResponseContainer implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // String을 포함한 모든 응답을 처리 (ApiResponse는 제외)
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request,
                                ServerHttpResponse response) {
        // String 타입일 경우 특별 처리
        if (body instanceof String) {
            // StringHttpMessageConverter가 사용될 때는 String을 반환해야 함
            if (selectedConverterType.equals(StringHttpMessageConverter.class)) {
                return body;
            }
        }

        // 이미 ApiResponse인 경우 그대로 반환
        if (body instanceof ApiResponse) {
            return body;
        }

        // 나머지는 성공 응답으로 변환
        return ApiResponse.success(body);
    }
} 