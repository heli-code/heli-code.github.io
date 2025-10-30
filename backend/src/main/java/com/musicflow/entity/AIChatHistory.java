package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_chat_history")
@Data
public class AIChatHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "user_message", nullable = false, columnDefinition = "TEXT")
    private String userMessage;
    
    @Column(name = "ai_response", nullable = false, columnDefinition = "TEXT")
    private String aiResponse;
    
    @Column(name = "context_data", columnDefinition = "JSON")
    private String contextData; // 对话上下文
    
    @Column(name = "emotion_context")
    private String emotionContext; // 情绪上下文
    
    @Column(name = "scene_context")
    private String sceneContext; // 场景上下文
    
    @Column(name = "chat_timestamp")
    private LocalDateTime chatTimestamp;
    
    @PrePersist
    protected void onCreate() {
        chatTimestamp = LocalDateTime.now();
    }
}