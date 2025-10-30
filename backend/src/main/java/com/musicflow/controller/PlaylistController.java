package com.musicflow.controller;

import com.musicflow.dto.ApiResponse;
import com.musicflow.dto.PlaylistDto;
import com.musicflow.dto.PlaylistRequest;
import com.musicflow.entity.Playlist;
import com.musicflow.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    
    private final PlaylistService playlistService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PlaylistDto>>> getPlaylists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PlaylistDto> playlists = playlistService.findAllPublic(pageable).map(PlaylistDto::fromEntity);
        return ResponseEntity.ok(ApiResponse.success("获取成功", playlists));
    }
    
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<PlaylistDto>>> getMyPlaylists(
            @RequestHeader("Authorization") String token) {
        
        // 临时实现 - 返回空列表
        List<PlaylistDto> playlists = List.of();
        
        return ResponseEntity.ok(ApiResponse.success("获取成功", playlists));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaylistDto>> getPlaylist(@PathVariable Long id) {
        // 临时实现 - 返回空对象
        return ResponseEntity.ok(ApiResponse.success("获取成功", null));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<PlaylistDto>> createPlaylist(
            @RequestHeader("Authorization") String token,
            @RequestBody PlaylistRequest request) {
        
        // 临时实现 - 返回空对象
        return ResponseEntity.ok(ApiResponse.success("创建成功", null));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaylistDto>> updatePlaylist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody PlaylistRequest request) {
        
        // 临时实现 - 返回空对象
        return ResponseEntity.ok(ApiResponse.success("更新成功", null));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePlaylist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        // 临时实现
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
    
    @PostMapping("/{id}/songs/{songId}")
    public ResponseEntity<ApiResponse<PlaylistDto>> addSongToPlaylist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @PathVariable Long songId) {
        
        // 临时实现 - 返回空对象
        return ResponseEntity.ok(ApiResponse.success("添加成功", null));
    }
    
    @DeleteMapping("/{id}/songs/{songId}")
    public ResponseEntity<ApiResponse<PlaylistDto>> removeSongFromPlaylist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @PathVariable Long songId) {
        
        // 临时实现 - 返回空对象
        return ResponseEntity.ok(ApiResponse.success("移除成功", null));
    }
    
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<PlaylistDto>> likePlaylist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        // 临时实现 - 返回空对象
        return ResponseEntity.ok(ApiResponse.success("点赞成功", null));
    }
    
    @PostMapping("/{id}/unlike")
    public ResponseEntity<ApiResponse<PlaylistDto>> unlikePlaylist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        // 临时实现 - 返回空对象
        return ResponseEntity.ok(ApiResponse.success("取消点赞成功", null));
    }
    
    @GetMapping("/{id}/liked")
    public ResponseEntity<ApiResponse<Boolean>> checkLiked(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        // 临时实现 - 返回false
        return ResponseEntity.ok(ApiResponse.success("获取成功", false));
    }
    
    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<PlaylistDto>>> getRecommendedPlaylists(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "5") int limit) {
        
        // 临时实现 - 返回空列表
        List<PlaylistDto> playlists = List.of();
        
        return ResponseEntity.ok(ApiResponse.success("推荐成功", playlists));
    }
    
    @GetMapping("/by-scene/{scene}")
    public ResponseEntity<ApiResponse<List<PlaylistDto>>> getPlaylistsByScene(
            @PathVariable String scene,
            @RequestParam(defaultValue = "10") int limit) {
        
        // 临时实现 - 返回空列表
        List<PlaylistDto> playlists = List.of();
        
        return ResponseEntity.ok(ApiResponse.success("获取成功", playlists));
    }
    
    @GetMapping("/hot")
    public ResponseEntity<ApiResponse<List<PlaylistDto>>> getHotPlaylists(
            @RequestParam(defaultValue = "10") int limit) {
        
        // 临时实现 - 返回空列表
        List<PlaylistDto> playlists = List.of();
        
        return ResponseEntity.ok(ApiResponse.success("获取成功", playlists));
    }
}