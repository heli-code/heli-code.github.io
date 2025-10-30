package com.musicflow.controller;

import com.musicflow.dto.ApiResponse;
import com.musicflow.util.FileUtil;
import com.musicflow.validator.ValidFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    
    private static final String UPLOAD_DIR = "uploads/";
    private static final String AUDIO_DIR = "uploads/audio/";
    private static final String IMAGE_DIR = "uploads/images/";
    
    private static final long MAX_AUDIO_SIZE = 50 * 1024 * 1024; // 50MB
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
    
    /**
     * 上传音频文件
     */
    @PostMapping("/audio")
    public ResponseEntity<ApiResponse<String>> uploadAudio(
            @RequestHeader("Authorization") String token,
            @ValidFile(fileType = FileType.AUDIO, maxSize = MAX_AUDIO_SIZE)
            @RequestParam("file") MultipartFile file) {
        
        try {
            if (!FileUtil.validateFileFormat(file.getOriginalFilename(), 
                    new String[]{"mp3", "wav", "flac", "aac"})) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("不支持的文件格式，仅支持mp3, wav, flac, aac格式"));
            }
            
            String filePath = FileUtil.saveFile(file, AUDIO_DIR);
            log.info("音频文件上传成功: {}", filePath);
            
            return ResponseEntity.ok(ApiResponse.success("音频文件上传成功", filePath));
            
        } catch (IOException e) {
            log.error("音频文件上传失败", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("文件上传失败: " + e.getMessage()));
        }
    }
    
    /**
     * 上传图片文件
     */
    @PostMapping("/image")
    public ResponseEntity<ApiResponse<String>> uploadImage(
            @RequestHeader("Authorization") String token,
            @ValidFile(fileType = FileType.IMAGE, maxSize = MAX_IMAGE_SIZE)
            @RequestParam("file") MultipartFile file) {
        
        try {
            if (!FileUtil.validateFileFormat(file.getOriginalFilename(), 
                    new String[]{"jpg", "jpeg", "png", "gif", "webp"})) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("不支持的文件格式，仅支持jpg, jpeg, png, gif, webp格式"));
            }
            
            String filePath = FileUtil.saveFile(file, IMAGE_DIR);
            log.info("图片文件上传成功: {}", filePath);
            
            return ResponseEntity.ok(ApiResponse.success("图片文件上传成功", filePath));
            
        } catch (IOException e) {
            log.error("图片文件上传失败", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("文件上传失败: " + e.getMessage()));
        }
    }
    
    /**
     * 下载文件
     */
    @GetMapping("/download/{filePath:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filePath) {
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filePath).normalize();
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = FileUtil.getMimeType(filePath);
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (MalformedURLException e) {
            log.error("文件下载失败", e);
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 预览文件
     */
    @GetMapping("/preview/{filePath:.+}")
    public ResponseEntity<Resource> previewFile(@PathVariable String filePath) {
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filePath).normalize();
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = FileUtil.getMimeType(filePath);
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (MalformedURLException e) {
            log.error("文件预览失败", e);
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除文件
     */
    @DeleteMapping("/{filePath:.+}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @RequestHeader("Authorization") String token,
            @PathVariable String filePath) {
        
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filePath).normalize();
            
            if (Files.exists(file)) {
                boolean deleted = Files.deleteIfExists(file);
                if (deleted) {
                    log.info("文件删除成功: {}", filePath);
                    return ResponseEntity.ok(ApiResponse.success("文件删除成功", null));
                }
            }
            
            return ResponseEntity.notFound().build();
            
        } catch (IOException e) {
            log.error("文件删除失败", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("文件删除失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取文件信息
     */
    @GetMapping("/info/{filePath:.+}")
    public ResponseEntity<ApiResponse<Object>> getFileInfo(@PathVariable String filePath) {
        try {
            Path file = Paths.get(UPLOAD_DIR).resolve(filePath).normalize();
            
            if (Files.exists(file)) {
                File fileObj = file.toFile();
                String contentType = FileUtil.getMimeType(filePath);
                
                Object fileInfo = new Object() {
                    public final String name = fileObj.getName();
                    public final String path = filePath;
                    public final long size = fileObj.length();
                    public final String sizeDescription = FileUtil.getFileSizeDescription(fileObj.length());
                    public final String contentType = contentType;
                    public final long lastModified = fileObj.lastModified();
                };
                
                return ResponseEntity.ok(ApiResponse.success("文件信息获取成功", fileInfo));
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("获取文件信息失败: " + e.getMessage()));
        }
    }
}