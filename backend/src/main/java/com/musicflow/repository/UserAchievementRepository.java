package com.musicflow.repository;

import com.musicflow.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    
    List<UserAchievement> findByUserId(Long userId);
    
    @Query("SELECT ua FROM UserAchievement ua WHERE ua.userId = :userId AND ua.achievementId = :achievementId")
    UserAchievement findByUserIdAndAchievementId(@Param("userId") Long userId, @Param("achievementId") Long achievementId);
    
    @Query("SELECT COUNT(ua) > 0 FROM UserAchievement ua WHERE ua.userId = :userId AND ua.achievementId = :achievementId")
    boolean existsByUserIdAndAchievementId(@Param("userId") Long userId, @Param("achievementId") Long achievementId);
}