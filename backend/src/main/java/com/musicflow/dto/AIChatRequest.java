package com.musicflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIChatRequest {
    private String message;
    private String context;
    private String sceneType;
    private String emotion;
    private Integer duration;
    private String[] preferredGenres;
}