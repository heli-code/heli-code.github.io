package com.musicflow.dto;

import com.musicflow.entity.Playlist;
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
public class PlaylistDto {
    private Long id;
    private String name;
    private String description;
    private String coverImage;
    private Integer songCount;
    private Integer playCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SongDto> songs;
    
    public static PlaylistDto fromEntity(Playlist playlist) {
        return PlaylistDto.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .coverImage(playlist.getCoverImage())
                .songCount(playlist.getSongCount())
                .playCount(playlist.getPlayCount())
                .createdAt(playlist.getCreatedAt())
                .updatedAt(playlist.getUpdatedAt())
                .build();
    }
    
    public static PlaylistDto fromEntityWithSongs(Playlist playlist, List<Song> songs) {
        PlaylistDto dto = fromEntity(playlist);
        dto.setSongs(songs.stream().map(SongDto::fromEntity).toList());
        return dto;
    }
}