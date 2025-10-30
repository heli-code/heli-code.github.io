package com.musicflow.util;

import com.musicflow.constant.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class FileUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    
    /**
     * 生成唯一的文件名
     */
    public static String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return timestamp + "_" + uuid + extension;
    }
    
    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    /**
     * 生成文件存储路径
     */
    public static String generateFilePath(String baseDir, String fileName) {
        String datePath = LocalDateTime.now().format(DATE_FORMATTER);
        return baseDir + datePath + "/" + fileName;
    }
    
    /**
     * 验证文件大小
     */
    public static boolean validateFileSize(MultipartFile file, long maxSize) {
        return file.getSize() <= maxSize;
    }
    
    /**
     * 验证文件格式
     */
    public static boolean validateFileFormat(String fileName, String[] allowedFormats) {
        String extension = getFileExtension(fileName).toLowerCase();
        if (extension.isEmpty()) {
            return false;
        }
        
        for (String format : allowedFormats) {
            if (("." + format).equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 保存文件
     */
    public static String saveFile(MultipartFile file, String baseDir) throws IOException {
        // 创建目录
        String datePath = LocalDateTime.now().format(DATE_FORMATTER);
        Path directory = Paths.get(baseDir, datePath);
        Files.createDirectories(directory);
        
        // 生成文件名
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        Path filePath = directory.resolve(fileName);
        
        // 保存文件
        file.transferTo(filePath.toFile());
        
        log.info("文件保存成功: {}", filePath.toString());
        return datePath + "/" + fileName;
    }
    
    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            log.error("删除文件失败: {}", filePath, e);
        }
        return false;
    }
    
    /**
     * 获取文件MIME类型
     */
    public static String getMimeType(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        
        switch (extension) {
            case ".mp3":
                return "audio/mpeg";
            case ".wav":
                return "audio/wav";
            case ".flac":
                return "audio/flac";
            case ".aac":
                return "audio/aac";
            case ".jpg":
            case ".jpeg":
                return "image/jpeg";
            case ".png":
                return "image/png";
            case ".gif":
                return "image/gif";
            case ".webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
    
    /**
     * 获取文件大小描述
     */
    public static String getFileSizeDescription(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}