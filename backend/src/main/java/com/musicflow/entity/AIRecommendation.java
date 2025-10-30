package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_recommendations")
@Data
public class AIRecommendation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_type", nullable = false)
    private RecommendationType recommendationType;
    
    @Column(name = "target_ids", columnDefinition = "JSON", nullable = false)
    private String targetIds; // 推荐目标ID列表
    
    @Column(name = "algorithm_type")
    private String algorithmType; // 推荐算法类型
    
    @Column(name = "confidence_score", precision = 4, scale = 3)
    private BigDecimal confidenceScore; // 推荐置信度
    
    @Column(name = "context_data", columnDefinition = "JSON")
    private String contextData; // 推荐上下文
    
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt; // 过期时间
    
    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
    
    public enum RecommendationType {
        SONG, PLAYLIST, ARTIST, GENRE
    }
}