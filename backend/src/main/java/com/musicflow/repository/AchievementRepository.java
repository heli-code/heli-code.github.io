package com.musicflow.repository;

import com.musicflow.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    
    List<Achievement> findByUserIdOrderByAchievedAtDesc(Long userId);
    
    @Query("SELECT a FROM Achievement a WHERE a.userId = :userId AND a.type = :type")
    List<Achievement> findByUserIdAndType(@Param("userId") Long userId, @Param("type") String type);
    
    @Query("SELECT a FROM Achievement a WHERE a.code = :code")
    Optional<Achievement> findByCode(@Param("code") String code);
}