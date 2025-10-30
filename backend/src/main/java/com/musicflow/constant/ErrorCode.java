package com.musicflow.constant;

/**
 * 错误码定义
 */
public enum ErrorCode {
    
    // 通用错误码
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),
    
    // 认证相关错误码
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    INVALID_PASSWORD(1003, "密码错误"),
    INVALID_TOKEN(1004, "无效的Token"),
    TOKEN_EXPIRED(1005, "Token已过期"),
    
    // 歌曲相关错误码
    SONG_NOT_FOUND(2001, "歌曲不存在"),
    SONG_ALREADY_EXISTS(2002, "歌曲已存在"),
    INVALID_AUDIO_FORMAT(2003, "不支持的音频格式"),
    AUDIO_FILE_TOO_LARGE(2004, "音频文件过大"),
    
    // 歌单相关错误码
    PLAYLIST_NOT_FOUND(3001, "歌单不存在"),
    PLAYLIST_ACCESS_DENIED(3002, "无权访问该歌单"),
    SONG_ALREADY_IN_PLAYLIST(3003, "歌曲已在歌单中"),
    
    // 文件相关错误码
    FILE_UPLOAD_FAILED(4001, "文件上传失败"),
    FILE_NOT_FOUND(4002, "文件不存在"),
    INVALID_FILE_FORMAT(4003, "不支持的文件格式"),
    FILE_SIZE_EXCEEDED(4004, "文件大小超出限制"),
    
    // 评论相关错误码
    COMMENT_NOT_FOUND(5001, "评论不存在"),
    COMMENT_ACCESS_DENIED(5002, "无权操作该评论"),
    
    // 数据库相关错误码
    DATABASE_ERROR(6001, "数据库操作失败"),
    DATA_VALIDATION_ERROR(6002, "数据验证失败"),
    
    // 网络相关错误码
    NETWORK_ERROR(7001, "网络连接错误"),
    SERVICE_UNAVAILABLE(7002, "服务不可用");
    
    private final int code;
    private final String message;
    
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return INTERNAL_ERROR;
    }
}