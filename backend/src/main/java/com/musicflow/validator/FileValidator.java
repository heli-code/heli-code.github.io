package com.musicflow.validator;

import com.musicflow.constant.AppConstants;
import com.musicflow.util.FileUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件验证器
 */
public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    
    private FileType fileType;
    private long maxSize;
    
    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.fileType = constraintAnnotation.fileType();
        this.maxSize = constraintAnnotation.maxSize();
    }
    
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        // 检查文件大小
        if (!FileUtil.validateFileSize(file, maxSize)) {
            return false;
        }
        
        // 检查文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }
        
        switch (fileType) {
            case AUDIO:
                return FileUtil.validateFileFormat(fileName, AppConstants.SUPPORTED_AUDIO_FORMATS);
            case IMAGE:
                return FileUtil.validateFileFormat(fileName, AppConstants.SUPPORTED_IMAGE_FORMATS);
            case ANY:
                return true;
            default:
                return false;
        }
    }
    
    /**
     * 文件类型枚举
     */
    public enum FileType {
        AUDIO, IMAGE, ANY
    }
}