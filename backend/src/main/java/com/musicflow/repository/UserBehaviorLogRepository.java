package com.musicflow.repository;

import com.musicflow.entity.UserBehaviorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserBehaviorLogRepository extends JpaRepository<UserBehaviorLog, Long> {
    
    List<UserBehaviorLog> findByUserIdOrderByTimestampDesc(Long userId);
    
    Page<UserBehaviorLog> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT ubl FROM UserBehaviorLog ubl WHERE ubl.user.id = :userId AND ubl.actionType = 'AI_CHAT' ORDER BY ubl.timestamp DESC LIMIT :limit")
    List<UserBehaviorLog> findRecentAIChats(@Param("userId") Long userId, @Param("limit") int limit);
    
    @Query("SELECT ubl FROM UserBehaviorLog ubl WHERE ubl.userId = :userId AND ubl.actionType = 'PLAY' AND ubl.timestamp >= :startTime")
    List<UserBehaviorLog> findRecentPlays(@Param("userId") Long userId, @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT ubl.targetId, COUNT(ubl) as playCount FROM UserBehaviorLog ubl WHERE ubl.actionType = 'PLAY' AND ubl.timestamp >= :startTime GROUP BY ubl.targetId ORDER BY playCount DESC")
    List<Object[]> findPopularSongs(@Param("startTime") LocalDateTime startTime, Pageable pageable);
    
    long countByUserIdAndActionType(Long userId, String actionType);
}