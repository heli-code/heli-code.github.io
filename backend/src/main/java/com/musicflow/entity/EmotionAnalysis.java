package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "emotion_analysis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionAnalysis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "emotion_type", nullable = false)
    private EmotionType emotionType;
    
    @Column(name = "confidence_score", precision = 4, scale = 3)
    private BigDecimal confidenceScore; // 置信度
    
    @Column(name = "trigger_context", columnDefinition = "JSON")
    private String triggerContext; // 触发上下文
    
    @Column(name = "text")
    private String text; // 分析文本内容
    
    @Column(name = "analyzed_at")
    private LocalDateTime analyzedAt;
    
    @PrePersist
    protected void onCreate() {
        analyzedAt = LocalDateTime.now();
    }
    
    public enum EmotionType {
        HAPPY, SAD, EXCITED, RELAXED, ENERGETIC, FOCUSED
    }
}