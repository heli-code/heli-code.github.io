package com.musicflow.repository;

import com.musicflow.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long>, JpaSpecificationExecutor<Song> {
    
    // 基础查询方法
    List<Song> findByTitleContainingIgnoreCase(String title);
    List<Song> findByArtistContainingIgnoreCase(String artist);
    List<Song> findByGenre(String genre);
    
    // 分页查询优化
    Page<Song> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Song> findByArtistContainingIgnoreCase(String artist, Pageable pageable);
    Page<Song> findByGenre(String genre, Pageable pageable);
    
    // 热门歌曲查询 - 使用原生SQL优化性能
    @Query(value = "SELECT * FROM songs ORDER BY play_count DESC LIMIT :limit", nativeQuery = true)
    List<Song> findTopSongsByPlayCount(@Param("limit") int limit);
    
    @Query(value = "SELECT * FROM songs ORDER BY like_count DESC LIMIT :limit", nativeQuery = true)
    List<Song> findTopSongsByLikeCount(@Param("limit") int limit);
    
    // 复杂搜索查询 - 多字段联合搜索
    @Query("SELECT s FROM Song s WHERE " +
           "LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.artist) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.album) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Song> searchSongs(@Param("keyword") String keyword);
    
    @Query("SELECT s FROM Song s WHERE " +
           "LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.artist) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.album) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.genre) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Song> searchSongs(@Param("keyword") String keyword, Pageable pageable);
    
    // 统计查询 - 使用缓存优化
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT COUNT(s) FROM Song s")
    long countAllSongs();
    
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @Query("SELECT COUNT(s) FROM Song s WHERE s.genre = :genre")
    long countSongsByGenre(@Param("genre") String genre);
    
    // 批量查询优化 - 避免N+1查询
    @Query("SELECT s FROM Song s LEFT JOIN FETCH s.moodTags WHERE s.id IN :ids")
    List<Song> findByIdsWithTags(@Param("ids") List<Long> ids);
    
    // 更新统计信息 - 使用原生SQL提高性能
    @Query(value = "UPDATE songs SET play_count = play_count + 1 WHERE id = :songId", nativeQuery = true)
    void incrementPlayCount(@Param("songId") Long songId);
    
    @Query(value = "UPDATE songs SET like_count = like_count + 1 WHERE id = :songId", nativeQuery = true)
    void incrementLikeCount(@Param("songId") Long songId);
    
    // 高级查询 - 根据BPM和情绪标签搜索
    @Query("SELECT s FROM Song s WHERE " +
           "s.bpm BETWEEN :minBpm AND :maxBpm AND " +
           "EXISTS (SELECT 1 FROM s.moodTags t WHERE t IN :moods)")
    List<Song> findSongsByBpmAndMood(@Param("minBpm") Integer minBpm, 
                                    @Param("maxBpm") Integer maxBpm, 
                                    @Param("moods") List<String> moods);
    
    // 获取歌曲详情 - 使用缓存和预加载
    @Query("SELECT s FROM Song s LEFT JOIN FETCH s.moodTags WHERE s.id = :id")
    Optional<Song> findByIdWithTags(@Param("id") Long id);
}