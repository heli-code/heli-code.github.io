package com.musicflow.repository;

import com.musicflow.entity.Playlist;
import com.musicflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUser(User user);
    List<Playlist> findByNameContainingIgnoreCase(String name);
    Page<Playlist> findByIsPublic(Boolean isPublic, Pageable pageable);
    
    default Page<Playlist> findByIsPublicTrue(Pageable pageable) {
        return findByIsPublic(true, pageable);
    }
}