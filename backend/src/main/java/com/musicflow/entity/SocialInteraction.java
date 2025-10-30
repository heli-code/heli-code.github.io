package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "social_interactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialInteraction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "target_id", nullable = false)
    private Long targetId;
    
    @Column(nullable = false)
    private String type;
    
    @Column(name = "content")
    private String content; // 互动内容
    
    @Column(name = "interaction_type")
    private String interactionType;
    
    @Column(name = "target_type", nullable = false)
    private String targetType;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}