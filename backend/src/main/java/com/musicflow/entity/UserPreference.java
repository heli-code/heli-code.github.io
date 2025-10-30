package com.musicflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserPreference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "preferred_genres", columnDefinition = "JSON")
    private String preferredGenres; // 偏好的音乐类型
    
    @Column(name = "preferred_artists", columnDefinition = "JSON")
    private String preferredArtists; // 偏好的艺术家
    
    @Column(name = "preferred_moods", columnDefinition = "JSON")
    private String preferredMoods; // 偏好的情绪类型
    
    @Column(name = "preferred_bpm_range")
    private String preferredBpmRange; // 偏好的BPM范围
    
    @Column(name = "scene_preferences", columnDefinition = "JSON")
    private String scenePreferences; // 场景偏好
    
    @Column(name = "listening_habits", columnDefinition = "JSON")
    private String listeningHabits; // 听歌习惯
    
    @Column(name = "emotion_patterns", columnDefinition = "JSON")
    private String emotionPatterns; // 情绪模式分析
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ai_personality_type")
    private AIPersonalityType aiPersonalityType = AIPersonalityType.FRIENDLY;
    
    @Column(name = "volume_level")
    private Integer volumeLevel = 80; // 音量级别
    
    @Column(name = "auto_play")
    private Boolean autoPlay = true; // 自动播放
    
    @Column(name = "recommendation_diversity")
    private Double recommendationDiversity = 0.7; // 推荐多样性
    
    @Column(name = "last_recommended_genres", columnDefinition = "JSON")
    private String lastRecommendedGenres; // 最近推荐的流派
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum AIPersonalityType {
        FRIENDLY, PROFESSIONAL, ENERGETIC
    }
}