package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "histories")
@Data
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;
    
    private LocalDateTime playedAt;
    
    private Integer duration; // 播放时长（秒）
    
    @PrePersist
    protected void onCreate() {
        playedAt = LocalDateTime.now();
    }
}