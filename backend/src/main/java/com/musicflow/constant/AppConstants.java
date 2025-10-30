package com.musicflow.constant;

/**
 * 应用常量定义
 */
public class AppConstants {
    
    // JWT相关常量
    public static final String JWT_SECRET = "musicflow-secret-key-2024";
    public static final long JWT_EXPIRATION = 86400000L; // 24小时
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    
    // 文件路径常量
    public static final String UPLOAD_DIR = "uploads/";
    public static final String AUDIO_DIR = "audio/";
    public static final String IMAGE_DIR = "images/";
    public static final String AVATAR_DIR = "avatars/";
    
    // 分页常量
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    
    // 缓存常量
    public static final String CACHE_PREFIX = "musicflow:";
    public static final long CACHE_DURATION = 3600L; // 1小时
    
    // 文件大小限制
    public static final long MAX_AUDIO_SIZE = 50 * 1024 * 1024L; // 50MB
    public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024L; // 5MB
    public static final long MAX_AVATAR_SIZE = 2 * 1024 * 1024L; // 2MB
    
    // 正则表达式
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    public static final String PASSWORD_REGEX = "^.{6,20}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    
    // 默认值
    public static final String DEFAULT_AVATAR = "👤";
    public static final String DEFAULT_COVER = "/images/default-cover.jpg";
    
    // 角色常量
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    
    // 音乐相关常量
    public static final String[] SUPPORTED_AUDIO_FORMATS = {"mp3", "wav", "flac", "aac"};
    public static final String[] SUPPORTED_IMAGE_FORMATS = {"jpg", "jpeg", "png", "gif", "webp"};
    
    // API路径常量
    public static final String API_PREFIX = "/api";
    public static final String AUTH_API = API_PREFIX + "/auth";
    public static final String SONGS_API = API_PREFIX + "/songs";
    public static final String PLAYLISTS_API = API_PREFIX + "/playlists";
    public static final String USERS_API = API_PREFIX + "/users";
    public static final String UPLOAD_API = API_PREFIX + "/upload";
}