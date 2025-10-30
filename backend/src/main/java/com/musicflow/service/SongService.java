package com.musicflow.service;

import com.musicflow.entity.Song;
import com.musicflow.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    
    private final SongRepository songRepository;
    
    public List<Song> findAll() {
        return songRepository.findAll();
    }
    
    public Song findById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("歌曲不存在"));
    }
    
    public List<Song> search(String keyword) {
        List<Song> byTitle = songRepository.findByTitleContainingIgnoreCase(keyword);
        List<Song> byArtist = songRepository.findByArtistContainingIgnoreCase(keyword);
        
        // 合并结果并去重
        byTitle.addAll(byArtist);
        return byTitle.stream().distinct().toList();
    }
    
    public List<Song> getPopularSongs() {
        return songRepository.findTop10ByPlayCount();
    }
    
    public List<Song> getTrendingSongs() {
        return songRepository.findTop10ByLikeCount();
    }
    
    public Song save(Song song) {
        return songRepository.save(song);
    }
    
    public void deleteById(Long id) {
        songRepository.deleteById(id);
    }
    
    public List<Song> findByMood(String mood, int limit) {
        // 简化实现 - 返回所有歌曲
        return songRepository.findAll().stream()
                .limit(limit)
                .toList();
    }
    
    public List<Song> findHotSongs(int limit) {
        // 简化实现 - 返回播放量最高的歌曲
        return songRepository.findAll().stream()
                .sorted((s1, s2) -> Integer.compare(s2.getPlayCount(), s1.getPlayCount()))
                .limit(limit)
                .toList();
    }
}