package com.musicflow.validator;

import com.musicflow.constant.AppConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * 密码验证器
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(AppConstants.PASSWORD_REGEX);
    
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // 初始化方法
    }
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        // 检查长度
        if (password.length() < 6 || password.length() > 20) {
            return false;
        }
        
        // 检查是否包含空格
        if (password.contains(" ")) {
            return false;
        }
        
        // 检查密码强度（可选）
        if (!isStrongPassword(password)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 检查密码强度
     */
    private boolean isStrongPassword(String password) {
        // 至少包含字母和数字
        boolean hasLetter = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
            
            if (hasLetter && hasDigit) {
                break;
            }
        }
        
        return hasLetter && hasDigit;
    }
}