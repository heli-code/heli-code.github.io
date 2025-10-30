package com.musicflow.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 用户名验证注解
 */
@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    
    String message() default "用户名格式不正确，只能包含字母、数字和下划线，长度3-20位";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}