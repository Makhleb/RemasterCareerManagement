package com.example.test.security.rim;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Created on 2024-12-30 by 구경림
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireToken {
    String[] roles() default {};
    boolean checkOwner() default false;
    boolean checkResume() default false;
} 