package com.musicflow.controller;

import com.musicflow.dto.ApiResponse;
import com.musicflow.dto.AIChatRequest;
import com.musicflow.dto.AIChatResponse;
import com.musicflow.dto.SongDto;
import com.musicflow.entity.EmotionAnalysis;
import com.musicflow.service.AIRecommendationService;
import com.musicflow.service.EmotionAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
    
    private final AIRecommendationService aiRecommendationService;
    private final EmotionAnalysisService emotionAnalysisService;
    
    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<AIChatResponse>> chatWithAI(
            @RequestHeader("Authorization") String token,
            @RequestBody AIChatRequest request) {
        
        AIChatResponse response = aiRecommendationService.chatWithAI(token, request);
        return ResponseEntity.ok(ApiResponse.success("AI回复成功", response));
    }
    
    @PostMapping("/emotion/analyze")
    public ResponseEntity<ApiResponse<EmotionAnalysis>> analyzeEmotion(
            @RequestBody String text) {
        
        EmotionAnalysis analysis = emotionAnalysisService.analyzeEmotion(text);
        return ResponseEntity.ok(ApiResponse.success("情绪分析成功", analysis));
    }
    
    @GetMapping("/scene/{sceneType}/recommendations")
    public ResponseEntity<ApiResponse<List<SongDto>>> getSceneRecommendations(
            @RequestHeader("Authorization") String token,
            @PathVariable String sceneType,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<SongDto> recommendations = aiRecommendationService
                .getSceneRecommendations(token, sceneType, limit)
                .stream()
                .map(SongDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("场景推荐成功", recommendations));
    }
    
    @GetMapping("/mood/{mood}/recommendations")
    public ResponseEntity<ApiResponse<List<SongDto>>> getMoodRecommendations(
            @RequestHeader("Authorization") String token,
            @PathVariable String mood,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<SongDto> recommendations = aiRecommendationService
                .getMoodRecommendations(token, mood, limit)
                .stream()
                .map(SongDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("情绪推荐成功", recommendations));
    }
    
    @GetMapping("/personalized/recommendations")
    public ResponseEntity<ApiResponse<List<SongDto>>> getPersonalizedRecommendations(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "15") int limit) {
        
        List<SongDto> recommendations = aiRecommendationService
                .getPersonalizedRecommendations(token, limit)
                .stream()
                .map(SongDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("个性化推荐成功", recommendations));
    }
    
    @GetMapping("/similar/{songId}")
    public ResponseEntity<ApiResponse<List<SongDto>>> getSimilarSongs(
            @PathVariable Long songId,
            @RequestParam(defaultValue = "8") int limit) {
        
        List<SongDto> similarSongs = aiRecommendationService
                .getSimilarSongs(songId, limit)
                .stream()
                .map(SongDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("相似歌曲推荐成功", similarSongs));
    }
    
    @GetMapping("/discover/new")
    public ResponseEntity<ApiResponse<List<SongDto>>> discoverNewMusic(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<SongDto> discoveries = aiRecommendationService
                .discoverNewMusic(token, limit)
                .stream()
                .map(SongDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("新音乐发现成功", discoveries));
    }
    
    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<SongDto>>> getTrendingRecommendations(
            @RequestParam(defaultValue = "10") int limit) {
        
        List<SongDto> trending = aiRecommendationService
                .getTrendingRecommendations(limit)
                .stream()
                .map(SongDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("热门推荐成功", trending));
    }
    
    @PostMapping("/feedback")
    public ResponseEntity<ApiResponse<Void>> provideFeedback(
            @RequestHeader("Authorization") String token,
            @RequestParam Long recommendationId,
            @RequestParam boolean positive) {
        
        aiRecommendationService.recordFeedback(token, recommendationId, positive);
        return ResponseEntity.ok(ApiResponse.success("反馈记录成功", null));
    }
    
    @GetMapping("/chat/history")
    public ResponseEntity<ApiResponse<List<AIChatResponse>>> getChatHistory(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "20") int limit) {
        
        List<AIChatResponse> history = aiRecommendationService.getChatHistory(token, limit);
        return ResponseEntity.ok(ApiResponse.success("聊天历史获取成功", history));
    }
}