package com.musicflow.service;

import com.musicflow.dto.AIChatRequest;
import com.musicflow.dto.AIChatResponse;
import com.musicflow.entity.Song;
import com.musicflow.entity.User;
import com.musicflow.entity.UserBehaviorLog;
import com.musicflow.repository.SongRepository;
import com.musicflow.repository.UserBehaviorLogRepository;
import com.musicflow.repository.UserRepository;
import com.musicflow.service.JwtService;
import com.musicflow.util.AIRecommendationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIRecommendationService {
    
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final UserBehaviorLogRepository behaviorLogRepository;
    private final JwtService jwtService;
    private final AIRecommendationUtil aiRecommendationUtil;
    
    public AIChatResponse chatWithAI(String token, AIChatRequest request) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 记录用户对话
        UserBehaviorLog behaviorLog = UserBehaviorLog.builder()
                .user(user)
                .actionType(UserBehaviorLog.ActionType.AI_CHAT)
                .targetType(UserBehaviorLog.TargetType.AI)
                .contextData("{\"userMessage\":\"" + request.getMessage() + 
                           "\",\"sceneType\":\"" + request.getSceneType() + 
                           "\",\"context\":\"" + request.getContext() + "\"}")
                .timestamp(LocalDateTime.now())
                .build();
        behaviorLogRepository.save(behaviorLog);
        
        // AI对话逻辑
        String aiResponse = generateAIResponse(user, request);
        List<Song> recommendedSongs = getRecommendationsBasedOnChat(user, request);
        
        return AIChatResponse.builder()
                .userMessage(request.getMessage())
                .aiResponse(aiResponse)
                .recommendedSongs(recommendedSongs)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public List<Song> getSceneRecommendations(String token, String sceneType, int limit) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return aiRecommendationUtil.getSceneBasedRecommendations(user, sceneType, limit);
    }
    
    public List<Song> getMoodRecommendations(String token, String mood, int limit) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return aiRecommendationUtil.getMoodBasedRecommendations(user, mood, limit);
    }
    
    public List<Song> getPersonalizedRecommendations(String token, int limit) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return aiRecommendationUtil.getPersonalizedRecommendations(user, limit);
    }
    
    public List<Song> getSimilarSongs(Long songId, int limit) {
        Song targetSong = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("歌曲不存在"));
        
        return aiRecommendationUtil.getSimilarSongs(targetSong, limit);
    }
    
    public List<Song> discoverNewMusic(String token, int limit) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        return aiRecommendationUtil.discoverNewMusic(user, limit);
    }
    
    public List<Song> getTrendingRecommendations(int limit) {
        return aiRecommendationUtil.getTrendingRecommendations(limit);
    }
    
    public void recordFeedback(String token, Long recommendationId, boolean positive) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 记录用户反馈，用于优化推荐算法
        UserBehaviorLog behaviorLog = UserBehaviorLog.builder()
                .user(user)
                .actionType(positive ? UserBehaviorLog.ActionType.POSITIVE_FEEDBACK : UserBehaviorLog.ActionType.NEGATIVE_FEEDBACK)
                .targetType(UserBehaviorLog.TargetType.RECOMMENDATION)
                .targetId(recommendationId)
                .contextData("{\"positive\":" + positive + "}")
                .timestamp(LocalDateTime.now())
                .build();
        behaviorLogRepository.save(behaviorLog);
        
        log.info("用户 {} 对推荐 {} 给出 {} 反馈", username, recommendationId, positive ? "正面" : "负面");
    }
    
    public List<AIChatResponse> getChatHistory(String token, int limit) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 获取最近的AI对话历史
        List<UserBehaviorLog> chatLogs = behaviorLogRepository
                .findRecentAIChats(user.getId(), limit);
        
        return chatLogs.stream()
                .map(this::convertToChatResponse)
                .toList();
    }
    
    private String generateAIResponse(User user, AIChatRequest request) {
        // 基于用户历史、当前场景和情绪生成AI回复
        StringBuilder response = new StringBuilder();
        
        // 根据场景类型生成不同的回复风格
        switch (request.getSceneType().toLowerCase()) {
            case "work":
                response.append("根据您的工作场景，我为您推荐一些专注音乐：\n");
                break;
            case "sport":
                response.append("运动时听这些高能量音乐会很棒：\n");
                break;
            case "relax":
                response.append("放松时刻，这些舒缓音乐很适合：\n");
                break;
            default:
                response.append("根据您的喜好，我为您推荐：\n");
        }
        
        // 添加个性化问候
        response.append("希望这些音乐能让您的心情更好！");
        
        return response.toString();
    }
    
    private List<Song> getRecommendationsBasedOnChat(User user, AIChatRequest request) {
        // 基于对话内容生成推荐
        String message = request.getMessage().toLowerCase();
        
        if (message.contains("工作") || message.contains("专注")) {
            return aiRecommendationUtil.getSceneBasedRecommendations(user, "work", 5);
        } else if (message.contains("运动") || message.contains("健身")) {
            return aiRecommendationUtil.getSceneBasedRecommendations(user, "sport", 5);
        } else if (message.contains("放松") || message.contains("休息")) {
            return aiRecommendationUtil.getSceneBasedRecommendations(user, "relax", 5);
        } else {
            return aiRecommendationUtil.getPersonalizedRecommendations(user, 5);
        }
    }
    
    private AIChatResponse convertToChatResponse(UserBehaviorLog log) {
        // 简化处理，直接返回基础响应
        return AIChatResponse.builder()
                .userMessage("用户消息")
                .aiResponse("AI回复内容")
                .timestamp(log.getTimestamp())
                .build();
    }
}