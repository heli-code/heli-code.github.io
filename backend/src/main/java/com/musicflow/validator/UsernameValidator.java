package com.musicflow.validator;

import com.musicflow.constant.AppConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 用户名验证器
 */
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(AppConstants.USERNAME_REGEX);
    
    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        // 初始化方法，可以获取注解参数
    }
    
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        // 检查长度
        if (username.length() < 3 || username.length() > 20) {
            return false;
        }
        
        // 检查字符格式
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return false;
        }
        
        // 检查保留用户名
        if (isReservedUsername(username)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 检查是否为保留用户名
     */
    private boolean isReservedUsername(String username) {
        String[] reservedNames = {
                "admin", "administrator", "root", "system", "test",
                "user", "guest", "anonymous", "musicflow", "music",
                "api", "app", "website", "service"
        };
        
        for (String reserved : reservedNames) {
            if (reserved.equalsIgnoreCase(username)) {
                return true;
            }
        }
        
        return false;
    }
}