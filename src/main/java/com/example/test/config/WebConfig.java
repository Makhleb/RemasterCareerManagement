package com.example.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


// @RestControllerAdvice와 ResponseBodyAdvice를 통해 자동으로 응답을 변환
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Bean
    public ResponseBodyAdvice<Object> responseWrapper() {
        return new ResponseContainer();
    }
} 