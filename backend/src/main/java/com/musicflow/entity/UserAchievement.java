package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_achievements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "achievement_id", nullable = false)
    private Long achievementId;
    
    @Column(name = "achievement")
    private String achievement;
    
    @Column(name = "achieved_at")
    private LocalDateTime achievedAt;
    
    @PrePersist
    protected void onCreate() {
        achievedAt = LocalDateTime.now();
    }
}