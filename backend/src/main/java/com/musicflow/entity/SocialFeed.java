package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "social_feeds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialFeed {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private String type;
    
    @Column(length = 2000)
    private String content;
    
    @Column(name = "song_id")
    private Long songId;
    
    @Column(name = "playlist_id")
    private Long playlistId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "like_count")
    private Integer likeCount = 0;
    
    @Column(name = "comment_count")
    private Integer commentCount = 0;
    
    @Column(name = "share_count")
    private Integer shareCount = 0;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}