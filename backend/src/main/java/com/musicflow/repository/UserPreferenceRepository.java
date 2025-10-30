package com.musicflow.repository;

import com.musicflow.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    
    Optional<UserPreference> findByUserId(Long userId);
    
    @Query("SELECT up FROM UserPreference up WHERE up.userId = :userId")
    Optional<UserPreference> findUserPreferenceByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(up) FROM UserPreference up WHERE up.preferredGenres LIKE %:genre%")
    long countByPreferredGenre(@Param("genre") String genre);
}