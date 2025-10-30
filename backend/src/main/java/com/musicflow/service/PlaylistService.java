package com.musicflow.service;

import com.musicflow.entity.Playlist;
import com.musicflow.entity.Song;
import com.musicflow.entity.User;
import com.musicflow.repository.PlaylistRepository;
import com.musicflow.repository.SongRepository;
import com.musicflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    
    public List<Playlist> getUserPlaylists(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return playlistRepository.findByUser(user);
    }
    
    public Playlist createPlaylist(Long userId, String name, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setUser(user);
        
        return playlistRepository.save(playlist);
    }
    
    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("歌单不存在"));
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("歌曲不存在"));
        
        if (!playlist.getSongs().contains(song)) {
            playlist.getSongs().add(song);
            playlist.setSongCount(playlist.getSongCount() + 1);
            return playlistRepository.save(playlist);
        }
        
        return playlist;
    }
    
    public void deletePlaylist(Long playlistId) {
        playlistRepository.deleteById(playlistId);
    }
    
    public List<Playlist> searchPlaylists(String keyword) {
        return playlistRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    public Page<Playlist> findAllPublic(Pageable pageable) {
        return playlistRepository.findByIsPublicTrue(pageable);
    }
}