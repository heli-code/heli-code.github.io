package com.musicflow.repository;

import com.musicflow.entity.SocialInteraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialInteractionRepository extends JpaRepository<SocialInteraction, Long> {
    
    Page<SocialInteraction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    @Query("SELECT si FROM SocialInteraction si WHERE si.userId IN :userIds ORDER BY si.createdAt DESC")
    Page<SocialInteraction> findByUserIdInOrderByCreatedAtDesc(@Param("userIds") List<Long> userIds, Pageable pageable);
    
    @Query("SELECT si FROM SocialInteraction si ORDER BY si.createdAt DESC LIMIT :limit")
    List<SocialInteraction> findTrendingInteractions(@Param("limit") int limit);
    
    long countByUserIdAndType(Long userId, String type);
    
    long countByUserId(Long userId);
    
    void deleteByUserIdAndTargetTypeAndTargetId(Long userId, String targetType, Long targetId);
    
    boolean existsByUserIdAndTargetTypeAndTargetId(Long userId, String targetType, Long targetId);
    
    @Query("SELECT si.targetId, COUNT(si) as interactionCount FROM SocialInteraction si WHERE si.type = :interactionType GROUP BY si.targetId ORDER BY interactionCount DESC")
    List<Object[]> findPopularContentByInteractionType(@Param("interactionType") String interactionType, Pageable pageable);
}