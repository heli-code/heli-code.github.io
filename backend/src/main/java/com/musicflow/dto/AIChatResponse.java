package com.musicflow.dto;

import com.musicflow.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIChatResponse {
    private String userMessage;
    private String aiResponse;
    private List<Song> recommendedSongs;
    private String emotionAnalysis;
    private String sceneSuggestion;
    private LocalDateTime timestamp;
}