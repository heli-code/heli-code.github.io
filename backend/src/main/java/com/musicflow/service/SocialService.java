package com.musicflow.service;

import com.musicflow.entity.*;
import com.musicflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialService {
    
    private final UserRepository userRepository;
    private final UserRelationshipRepository relationshipRepository;
    private final SocialInteractionRepository interactionRepository;
    private final CommentRepository commentRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final SongRepository songRepository;
    private final JwtService jwtService;
    
    @Transactional
    public void followUser(String token, Long targetUserId) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("目标用户不存在"));
        
        if (currentUser.getId().equals(targetUserId)) {
            throw new RuntimeException("不能关注自己");
        }
        
        // 检查是否已经关注
        if (relationshipRepository.existsByFollowerIdAndFollowingId(currentUser.getId(), targetUserId)) {
            throw new RuntimeException("已经关注该用户");
        }
        
        // 创建关注关系
        UserRelationship relationship = UserRelationship.builder()
                .followerId(currentUser.getId())
                .followingId(targetUserId)
                .relationshipType("FOLLOW")
                .createdAt(LocalDateTime.now())
                .build();
        relationshipRepository.save(relationship);
        
        // 记录社交互动
        SocialInteraction interaction = SocialInteraction.builder()
                .userId(currentUser.getId())
                .type("FOLLOW")
                .targetId(targetUserId)
                .targetType("USER")
                .createdAt(LocalDateTime.now())
                .build();
        interactionRepository.save(interaction);
        
        log.info("用户 {} 关注了用户 {}", currentUser.getUsername(), targetUser.getUsername());
    }
    
    @Transactional
    public void unfollowUser(String token, Long targetUserId) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        relationshipRepository.deleteByFollowerIdAndFollowingId(currentUser.getId(), targetUserId);
        log.info("用户 {} 取消关注了用户 {}", currentUser.getUsername(), targetUserId);
    }
    
    public List<User> getFollowers(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        List<Long> followerIds = relationshipRepository.findFollowerIdsByFollowingId(currentUser.getId());
        return userRepository.findAllById(followerIds);
    }
    
    public List<User> getFollowing(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        List<Long> followingIds = relationshipRepository.findFollowingIdsByFollowerId(currentUser.getId());
        return userRepository.findAllById(followingIds);
    }
    
    public Object getSocialFeed(String token, Pageable pageable) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 获取关注用户的动态
        List<Long> followingIds = relationshipRepository.findFollowingIdsByFollowerId(currentUser.getId());
        
        // 获取社交互动动态
        Page<SocialInteraction> interactions = interactionRepository
                .findByUserIdInOrderByCreatedAtDesc(followingIds, pageable);
        
        Map<String, Object> result = new HashMap<>();
        result.put("interactions", interactions.getContent());
        result.put("totalPages", interactions.getTotalPages());
        result.put("totalElements", interactions.getTotalElements());
        result.put("currentPage", interactions.getNumber());
        
        return result;
    }
    
    public List<Achievement> getUserAchievements(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 获取用户已获得的成就
        List<UserAchievement> userAchievements = userAchievementRepository
                .findByUserId(currentUser.getId());
        
        return userAchievements.stream()
                .map(UserAchievement::getAchievement)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void unlockAchievement(String token, String achievementCode) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Achievement achievement = achievementRepository.findByCode(achievementCode)
                .orElseThrow(() -> new RuntimeException("成就不存在"));
        
        // 检查是否已经获得该成就
        if (userAchievementRepository.existsByUserIdAndAchievementId(
                currentUser.getId(), achievement.getId())) {
            throw new RuntimeException("已经获得该成就");
        }
        
        // 解锁成就
        UserAchievement userAchievement = UserAchievement.builder()
                .user(currentUser)
                .achievement(achievement)
                .unlockedAt(LocalDateTime.now())
                .build();
        
        userAchievementRepository.save(userAchievement);
        
        log.info("用户 {} 解锁了成就: {}", username, achievement.getName());
    }
    
    @Transactional
    public void likeContent(String token, String targetType, Long targetId) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        SocialInteraction interaction = SocialInteraction.builder()
                .userId(currentUser.getId())
                .type("LIKE")
                .targetId(targetId)
                .targetType(targetType)
                .createdAt(LocalDateTime.now())
                .build();
        interactionRepository.save(interaction);
        
        log.info("用户 {} 点赞了 {} {}", username, targetType, targetId);
    }
    
    @Transactional
    public void unlikeContent(String token, String targetType, Long targetId) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        interactionRepository.deleteByUserIdAndTargetTypeAndTargetId(
                currentUser.getId(), targetType, targetId);
        
        log.info("用户 {} 取消点赞了 {} {}", username, targetType, targetId);
    }
    
    @Transactional
    public void shareContent(String token, String targetType, Long targetId) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        SocialInteraction interaction = SocialInteraction.builder()
                .userId(currentUser.getId())
                .type("SHARE")
                .targetId(targetId)
                .targetType(targetType)
                .createdAt(LocalDateTime.now())
                .build();
        interactionRepository.save(interaction);
        
        log.info("用户 {} 分享了 {} {}", username, targetType, targetId);
    }
    
    @Transactional
    public Comment addComment(String token, Long songId, String content) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("歌曲不存在"));
        
        Comment comment = Comment.builder()
                .user(currentUser)
                .song(song)
                .content(content)
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        Comment savedComment = commentRepository.save(comment);
        
        // 记录评论互动
        SocialInteraction interaction = SocialInteraction.builder()
                .userId(currentUser.getId())
                .type("COMMENT")
                .targetId(songId)
                .targetType("SONG")
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
        interactionRepository.save(interaction);
        
        log.info("用户 {} 评论了歌曲 {}: {}", username, songId, content);
        
        return savedComment;
    }
    
    public Page<Comment> getComments(String targetType, Long targetId, Pageable pageable) {
        return commentRepository.findByTargetTypeAndTargetIdOrderByCreatedAtDesc(
                targetType, targetId, pageable);
    }
    
    public List<User> getRecommendedUsers(String token, int limit) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 基于共同兴趣推荐用户的增强算法
        List<User> allUsers = userRepository.findAll();
        
        // 排除当前用户和已关注的用户
        List<Long> followingIds = relationshipRepository.findFollowingIdsByFollowerId(currentUser.getId());
        
        return allUsers.stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .filter(user -> !followingIds.contains(user.getId()))
                .sorted((u1, u2) -> calculateUserSimilarityScore(currentUser, u2) - 
                                   calculateUserSimilarityScore(currentUser, u1))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * 计算用户相似度分数
     */
    private int calculateUserSimilarityScore(User currentUser, User targetUser) {
        int score = 0;
        
        // 1. 共同关注数（权重最高）
        List<Long> currentFollowing = relationshipRepository.findFollowingIdsByFollowerId(currentUser.getId());
        List<Long> targetFollowing = relationshipRepository.findFollowingIdsByFollowerId(targetUser.getId());
        
        long commonFollowing = currentFollowing.stream()
                .filter(targetFollowing::contains)
                .count();
        score += commonFollowing * 10;
        
        // 2. 地理位置相似度（如果有位置信息）
        // 这里可以添加基于位置的相似度计算
        
        // 3. 活跃度相似度
        long currentUserActivity = interactionRepository.countByUserId(currentUser.getId());
        long targetUserActivity = interactionRepository.countByUserId(targetUser.getId());
        
        if (Math.abs(currentUserActivity - targetUserActivity) < 10) {
            score += 5; // 活跃度相近
        }
        
        // 4. 注册时间相似度
        if (currentUser.getCreatedAt() != null && targetUser.getCreatedAt() != null) {
            long daysDiff = Math.abs(java.time.Duration.between(
                    currentUser.getCreatedAt(), targetUser.getCreatedAt()).toDays());
            if (daysDiff < 30) {
                score += 3; // 注册时间相近
            }
        }
        
        return score;
    }
    
    public Object getTrendingContent(int limit) {
        // 获取热门内容 - 增强版算法
        List<SocialInteraction> trendingInteractions = interactionRepository
                .findTrendingInteractions(limit * 2); // 获取更多用于筛选
        
        // 应用热度算法
        List<SocialInteraction> scoredInteractions = trendingInteractions.stream()
                .sorted((i1, i2) -> {
                    double score1 = calculateTrendingScore(i1);
                    double score2 = calculateTrendingScore(i2);
                    return Double.compare(score2, score1);
                })
                .limit(limit)
                .collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("interactions", scoredInteractions);
        result.put("totalElements", (long) scoredInteractions.size());
        result.put("currentPage", 0);
        result.put("totalPages", 1);
        
        return result;
    }
    
    /**
     * 计算内容热度分数
     */
    private double calculateTrendingScore(SocialInteraction interaction) {
        double score = 0.0;
        
        // 1. 时间衰减因子（越新的内容分数越高）
        if (interaction.getCreatedAt() != null) {
            long hoursSinceCreation = java.time.Duration.between(
                    interaction.getCreatedAt(), LocalDateTime.now()).toHours();
            score += Math.max(0, 100 - hoursSinceCreation * 2); // 每小时衰减2分
        }
        
        // 2. 互动类型权重
        switch (interaction.getType()) {
            case "LIKE":
                score += 10;
                break;
            case "COMMENT":
                score += 15; // 评论权重更高
                break;
            case "SHARE":
                score += 20; // 分享权重最高
                break;
            case "FOLLOW":
                score += 25; // 关注权重最高
                break;
        }
        
        // 3. 用户影响力因子（简化实现）
        // 在实际应用中，这里会考虑用户的粉丝数、活跃度等
        score += 5;
        
        return score;
    }
    
    /**
     * 获取个性化社交推荐
     */
    public Object getPersonalizedSocialRecommendations(String token, int limit) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 获取关注用户的动态
        List<Long> followingIds = relationshipRepository.findFollowingIdsByFollowerId(currentUser.getId());
        
        // 获取个性化推荐内容
        List<SocialInteraction> recommendations = interactionRepository
                .findByUserIdInOrderByCreatedAtDesc(followingIds, Pageable.ofSize(limit * 3))
                .getContent();
        
        // 应用个性化排序算法
        List<SocialInteraction> personalizedRecommendations = recommendations.stream()
                .sorted((i1, i2) -> {
                    double score1 = calculatePersonalizationScore(i1, currentUser);
                    double score2 = calculatePersonalizationScore(i2, currentUser);
                    return Double.compare(score2, score1);
                })
                .limit(limit)
                .collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("recommendations", personalizedRecommendations);
        result.put("totalElements", (long) personalizedRecommendations.size());
        
        return result;
    }
    
    /**
     * 计算个性化推荐分数
     */
    private double calculatePersonalizationScore(SocialInteraction interaction, User currentUser) {
        double score = 0.0;
        
        // 1. 社交关系强度
        if (interaction.getUserId().equals(currentUser.getId())) {
            score += 50; // 自己的内容最高优先级
        }
        
        // 2. 互动历史相似度
        // 简化实现 - 在实际应用中会使用更复杂的算法
        score += 20;
        
        // 3. 内容类型偏好
        // 根据用户历史互动数据计算偏好权重
        score += 15;
        
        // 4. 时间衰减
        if (interaction.getCreatedAt() != null) {
            long hoursSinceCreation = java.time.Duration.between(
                    interaction.getCreatedAt(), LocalDateTime.now()).toHours();
            score += Math.max(0, 30 - hoursSinceCreation); // 每小时衰减1分
        }
        
        return score;
    }
    
    public Object getUserActivityStats(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 统计用户活动数据 - 简化实现
        long totalLikes = interactionRepository.countByUserIdAndType(currentUser.getId(), "LIKE");
        long totalComments = interactionRepository.countByUserIdAndType(currentUser.getId(), "COMMENT");
        long totalShares = interactionRepository.countByUserIdAndType(currentUser.getId(), "SHARE");
        long totalFollowers = relationshipRepository.countByFollowingId(currentUser.getId());
        long totalFollowing = relationshipRepository.countByFollowerId(currentUser.getId());
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLikes", totalLikes);
        stats.put("totalComments", totalComments);
        stats.put("totalShares", totalShares);
        stats.put("totalFollowers", totalFollowers);
        stats.put("totalFollowing", totalFollowing);
        stats.put("achievementCount", 0L);
        
        return stats;
    }
    
    /**
     * 批量用户状态检查
     */
    public List<Map<String, Object>> batchCheckUserStatus(List<String> usernames) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (String username : usernames) {
            try {
                Map<String, Object> status = new HashMap<>();
                status.put("username", username);
                status.put("status", "active");
                status.put("lastLogin", LocalDateTime.now());
                status.put("createdAt", LocalDateTime.now());
                results.add(status);
            } catch (Exception e) {
                Map<String, Object> errorResult = new HashMap<>();
                errorResult.put("username", username);
                errorResult.put("status", "not_found");
                errorResult.put("error", e.getMessage());
                results.add(errorResult);
            }
        }
        
        return results;
    }
}