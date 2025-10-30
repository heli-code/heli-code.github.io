package com.musicflow.dto;

import com.musicflow.entity.Song;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    private Long id;
    private String title;
    private String artist;
    private String album;
    private Integer duration;
    private String genre;
    private List<String> moodTags;
    private Integer bpm;
    private String audioUrl;
    private String coverImage;
    private Integer playCount;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static SongDto fromEntity(Song song) {
        return SongDto.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artist(song.getArtist())
                .album(song.getAlbum())
                .duration(song.getDuration())
                .genre(song.getGenre())
                .moodTags(song.getMoodTags())
                .bpm(song.getBpm())
                .audioUrl(song.getAudioUrl())
                .coverImage(song.getCoverImage())
                .playCount(song.getPlayCount())
                .likeCount(song.getLikeCount())
                .createdAt(song.getCreatedAt())
                .updatedAt(song.getUpdatedAt())
                .build();
    }
}