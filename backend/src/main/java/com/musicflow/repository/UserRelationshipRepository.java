package com.musicflow.repository;

import com.musicflow.entity.UserRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {
    
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    @Query("SELECT ur.followingId FROM UserRelationship ur WHERE ur.followerId = :followerId")
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);
    
    @Query("SELECT ur.followerId FROM UserRelationship ur WHERE ur.followingId = :followingId")
    List<Long> findFollowerIdsByFollowingId(@Param("followingId") Long followingId);
    
    long countByFollowerId(Long followerId);
    
    long countByFollowingId(Long followingId);
    
    @Query("SELECT ur FROM UserRelationship ur WHERE ur.followerId = :userId OR ur.followingId = :userId")
    List<UserRelationship> findByUserId(@Param("userId") Long userId);
}