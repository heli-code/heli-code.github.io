package com.musicflow.controller;

import com.musicflow.dto.ApiResponse;
import com.musicflow.dto.SocialFeedDto;
import com.musicflow.dto.UserProfileDto;
import com.musicflow.entity.Achievement;
import com.musicflow.entity.Comment;
import com.musicflow.entity.SocialFeed;
import com.musicflow.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/social")
@RequiredArgsConstructor
public class SocialController {
    
    private final SocialService socialService;
    
    @PostMapping("/follow/{targetUserId}")
    public ResponseEntity<ApiResponse<Void>> followUser(
            @RequestHeader("Authorization") String token,
            @PathVariable Long targetUserId) {
        
        socialService.followUser(token, targetUserId);
        return ResponseEntity.ok(ApiResponse.success("关注成功", null));
    }
    
    @PostMapping("/unfollow/{targetUserId}")
    public ResponseEntity<ApiResponse<Void>> unfollowUser(
            @RequestHeader("Authorization") String token,
            @PathVariable Long targetUserId) {
        
        socialService.unfollowUser(token, targetUserId);
        return ResponseEntity.ok(ApiResponse.success("取消关注成功", null));
    }
    
    @GetMapping("/followers")
    public ResponseEntity<ApiResponse<List<UserProfileDto>>> getFollowers(
            @RequestHeader("Authorization") String token) {
        
        List<UserProfileDto> followers = socialService.getFollowers(token).stream()
                .map(UserProfileDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("获取成功", followers));
    }
    
    @GetMapping("/following")
    public ResponseEntity<ApiResponse<List<UserProfileDto>>> getFollowing(
            @RequestHeader("Authorization") String token) {
        
        List<UserProfileDto> following = socialService.getFollowing(token).stream()
                .map(UserProfileDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("获取成功", following));
    }
    
    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<Object>> getSocialFeed(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Object feed = socialService.getSocialFeed(token, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("获取动态成功", feed));
    }
    
    @GetMapping("/achievements")
    public ResponseEntity<ApiResponse<List<Achievement>>> getUserAchievements(
            @RequestHeader("Authorization") String token) {
        
        List<Achievement> achievements = socialService.getUserAchievements(token);
        return ResponseEntity.ok(ApiResponse.success("获取成就成功", achievements));
    }
    
    @PostMapping("/{targetType}/{targetId}/like")
    public ResponseEntity<ApiResponse<Void>> likeContent(
            @RequestHeader("Authorization") String token,
            @PathVariable String targetType,
            @PathVariable Long targetId) {
        
        socialService.likeContent(token, targetType, targetId);
        return ResponseEntity.ok(ApiResponse.success("点赞成功", null));
    }
    
    @PostMapping("/{targetType}/{targetId}/unlike")
    public ResponseEntity<ApiResponse<Void>> unlikeContent(
            @RequestHeader("Authorization") String token,
            @PathVariable String targetType,
            @PathVariable Long targetId) {
        
        socialService.unlikeContent(token, targetType, targetId);
        return ResponseEntity.ok(ApiResponse.success("取消点赞成功", null));
    }
    
    @PostMapping("/{targetType}/{targetId}/share")
    public ResponseEntity<ApiResponse<Void>> shareContent(
            @RequestHeader("Authorization") String token,
            @PathVariable String targetType,
            @PathVariable Long targetId) {
        
        socialService.shareContent(token, targetType, targetId);
        return ResponseEntity.ok(ApiResponse.success("分享成功", null));
    }
    
    @PostMapping("/song/{songId}/comment")
    public ResponseEntity<ApiResponse<Comment>> addComment(
            @RequestHeader("Authorization") String token,
            @PathVariable Long songId,
            @RequestBody String content) {
        
        Comment comment = socialService.addComment(token, songId, content);
        return ResponseEntity.ok(ApiResponse.success("评论成功", comment));
    }
    
    @GetMapping("/{targetType}/{targetId}/comments")
    public ResponseEntity<ApiResponse<Page<Comment>>> getComments(
            @PathVariable String targetType,
            @PathVariable Long targetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = socialService.getComments(targetType, targetId, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("获取评论成功", comments));
    }
    
    @GetMapping("/recommended/users")
    public ResponseEntity<ApiResponse<List<UserProfileDto>>> getRecommendedUsers(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "5") int limit) {
        
        List<UserProfileDto> users = socialService.getRecommendedUsers(token, limit).stream()
                .map(UserProfileDto::fromEntity)
                .toList();
        
        return ResponseEntity.ok(ApiResponse.success("推荐用户获取成功", users));
    }
    
    @GetMapping("/trending/content")
    public ResponseEntity<ApiResponse<Object>> getTrendingContent(
            @RequestParam(defaultValue = "10") int limit) {
        
        Object trending = socialService.getTrendingContent(limit);
        return ResponseEntity.ok(ApiResponse.success("热门内容获取成功", trending));
    }
    
    @GetMapping("/activity/stats")
    public ResponseEntity<ApiResponse<Object>> getUserActivityStats(
            @RequestHeader("Authorization") String token) {
        
        Object stats = socialService.getUserActivityStats(token);
        return ResponseEntity.ok(ApiResponse.success("用户活动统计获取成功", stats));
    }
}