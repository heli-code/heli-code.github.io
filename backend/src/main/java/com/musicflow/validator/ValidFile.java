package com.musicflow.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 文件验证注解
 */
@Documented
@Constraint(validatedBy = FileValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {
    
    String message() default "文件格式不正确或文件过大";
    
    FileValidator.FileType fileType() default FileValidator.FileType.ANY;
    
    long maxSize() default 10 * 1024 * 1024; // 默认10MB
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}