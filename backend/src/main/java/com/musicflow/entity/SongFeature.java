package com.musicflow.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "song_features")
@Data
public class SongFeature {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;
    
    @Column(precision = 5, scale = 4)
    private BigDecimal acousticness; // 原声度
    
    @Column(precision = 5, scale = 4)
    private BigDecimal danceability; // 可舞性
    
    @Column(precision = 5, scale = 4)
    private BigDecimal energy; // 能量
    
    @Column(precision = 5, scale = 4)
    private BigDecimal instrumentalness; // 器乐性
    
    @Column(precision = 5, scale = 4)
    private BigDecimal liveness; // 现场感
    
    @Column(precision = 5, scale = 4)
    private BigDecimal speechiness; // 语音度
    
    @Column(precision = 5, scale = 4)
    private BigDecimal valence; // 愉悦度
    
    @Column(precision = 6, scale = 2)
    private BigDecimal tempo; // 速度
    
    private Integer key; // 调性
    
    private Integer mode; // 调式
    
    @Column(precision = 5, scale = 2)
    private BigDecimal loudness; // 响度
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}