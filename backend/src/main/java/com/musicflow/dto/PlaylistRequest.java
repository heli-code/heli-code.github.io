package com.musicflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlaylistRequest {
    
    @NotBlank(message = "歌单名称不能为空")
    @Size(max = 100, message = "歌单名称不能超过100个字符")
    private String name;
    
    @Size(max = 500, message = "歌单描述不能超过500个字符")
    private String description;
    
    private boolean isPublic = true;
}