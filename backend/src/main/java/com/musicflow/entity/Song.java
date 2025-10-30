package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "songs")
@Data
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String artist;
    
    private String album;
    
    private String coverImage;
    
    @Column(nullable = false)
    private String audioUrl;
    
    private Integer duration; // 秒数
    
    private String genre;
    
    private Integer bpm;
    
    @ElementCollection
    private List<String> moodTags;
    
    private Integer playCount = 0;
    
    private Integer likeCount = 0;
    
    private LocalDateTime createdAt;
    
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
}