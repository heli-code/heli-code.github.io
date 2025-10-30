package com.musicflow.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 密码验证注解
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    
    String message() default "密码格式不正确，必须包含字母和数字，长度6-20位，不能包含空格";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}