package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_behavior_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBehaviorLog {
    
    // 显式添加getter方法确保编译通过
    public Long getId() {
        return id;
    }
    
    public User getUser() {
        return user;
    }
    
    public ActionType getActionType() {
        return actionType;
    }
    
    public Long getTargetId() {
        return targetId;
    }
    
    public TargetType getTargetType() {
        return targetType;
    }
    
    public String getContextData() {
        return contextData;
    }
    
    public String getEmotionContext() {
        return emotionContext;
    }
    
    public String getSceneContext() {
        return sceneContext;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;
    
    private Long targetId; // 目标ID（歌曲、歌单等）
    
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;
    
    @Column(name = "context_data", columnDefinition = "TEXT")
    private String contextData; // 上下文数据
    
    @Column(name = "emotion_context")
    private String emotionContext; // 情绪上下文
    
    @Column(name = "scene_context")
    private String sceneContext; // 场景上下文
    
    @Column(name = "duration")
    private Integer duration; // 行为持续时间（秒）
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
    
    public enum ActionType {
        PLAY, PAUSE, SKIP, LIKE, SHARE, DOWNLOAD, COMMENT, SEARCH, AI_CHAT, POSITIVE_FEEDBACK, NEGATIVE_FEEDBACK
    }
    
    public enum TargetType {
        SONG, PLAYLIST, ALBUM, USER, AI, RECOMMENDATION
    }
}