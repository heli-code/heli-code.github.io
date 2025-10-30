package com.musicflow.dto;

import com.musicflow.entity.SocialFeed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialFeedDto {
    
    private Long id;
    private Long userId;
    private String type;
    private String content;
    private Long songId;
    private Long playlistId;
    private LocalDateTime createdAt;
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;
    
    public static SocialFeedDto fromEntity(SocialFeed feed) {
        return SocialFeedDto.builder()
                .id(feed.getId())
                .userId(feed.getUserId())
                .type(feed.getType())
                .content(feed.getContent())
                .songId(feed.getSongId())
                .playlistId(feed.getPlaylistId())
                .createdAt(feed.getCreatedAt())
                .likeCount(feed.getLikeCount())
                .commentCount(feed.getCommentCount())
                .shareCount(feed.getShareCount())
                .build();
    }
}