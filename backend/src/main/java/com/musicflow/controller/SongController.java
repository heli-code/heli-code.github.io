package com.musicflow.controller;

import com.musicflow.dto.ApiResponse;
import com.musicflow.dto.SongDto;
import com.musicflow.entity.Song;
import com.musicflow.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {
    
    private final SongService songService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<SongDto>>> getSongs() {
        List<SongDto> songs = songService.findAll().stream()
                .map(SongDto::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("获取成功", songs));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SongDto>> getSong(@PathVariable Long id) {
        Song song = songService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("获取成功", SongDto.fromEntity(song)));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SongDto>>> searchSongs(@RequestParam String keyword) {
        List<SongDto> songs = songService.search(keyword).stream()
                .map(SongDto::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("搜索成功", songs));
    }
    
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<SongDto>>> getPopularSongs() {
        List<SongDto> songs = songService.getPopularSongs().stream()
                .map(SongDto::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("获取成功", songs));
    }
    
    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<SongDto>>> getTrendingSongs() {
        List<SongDto> songs = songService.getTrendingSongs().stream()
                .map(SongDto::fromEntity)
                .toList();
        return ResponseEntity.ok(ApiResponse.success("获取成功", songs));
    }
    
    @GetMapping("/by-mood/{mood}")
    public ResponseEntity<ApiResponse<List<SongDto>>> getSongsByMood(
            @PathVariable String mood,
            @RequestParam(defaultValue = "20") int limit) {
        
        List<SongDto> songs = songService.findByMood(mood, limit).stream()
                .map(SongDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("获取成功", songs));
    }
    
    @GetMapping("/hot")
    public ResponseEntity<ApiResponse<List<SongDto>>> getHotSongs(
            @RequestParam(defaultValue = "10") int limit) {
        
        List<SongDto> songs = songService.findHotSongs(limit).stream()
                .map(SongDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("获取成功", songs));
    }
    
    @PostMapping("/{id}/play")
    public ResponseEntity<ApiResponse<Void>> recordPlay(@PathVariable Long id) {
        Song song = songService.findById(id);
        // 简单的播放计数增加
        song.setPlayCount(song.getPlayCount() + 1);
        songService.save(song);
        return ResponseEntity.ok(ApiResponse.success("播放记录成功", null));
    }
}