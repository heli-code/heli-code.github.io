package com.musicflow.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String nickname;
    private String avatar;
    private String email;
}