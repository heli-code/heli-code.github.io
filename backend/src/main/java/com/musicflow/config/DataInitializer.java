package com.musicflow.config;

import com.musicflow.entity.*;
import com.musicflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    public CommandLineRunner initData() {
        return args -> {
            if (userRepository.count() == 0) {
                log.info("开始初始化数据库数据...");
                initializeUsers();
                initializeSongs();
                initializePlaylists();
                log.info("数据库数据初始化完成！");
            }
        };
    }

    private void initializeUsers() {
        List<User> users = Arrays.asList(
            createUser("admin", "admin@musicflow.com", "管理员", "ADMIN"),
            createUser("musiclover", "musiclover@musicflow.com", "音乐爱好者", "USER"),
            createUser("popfan", "popfan@musicflow.com", "流行音乐迷", "USER"),
            createUser("rockstar", "rockstar@musicflow.com", "摇滚之星", "VIP"),
            createUser("jazzcat", "jazzcat@musicflow.com", "爵士猫", "USER")
        );
        
        userRepository.saveAll(users);
        log.info("初始化 {} 个用户数据", users.size());
    }

    private User createUser(String username, String email, String nickname, String role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setNickname(nickname);
        user.setRole(User.UserRole.valueOf(role));
        user.setAvatar("/avatars/" + username + ".jpg");
        return user;
    }

    private void initializeSongs() {
        List<Song> songs = Arrays.asList(
            createSong("Blinding Lights", "The Weeknd", 200, "Pop", 
                       "[\"Energetic\", \"Nostalgic\", \"Dance\"]", 171, 1L),
            createSong("Save Your Tears", "The Weeknd", 215, "Pop", 
                       "[\"Emotional\", \"Melancholic\", \"Dance\"]", 118, 1L),
            createSong("Bad Guy", "Billie Eilish", 194, "Pop", 
                       "[\"Dark\", \"Edgy\", \"Confident\"]", 135, 2L),
            createSong("Everything I Wanted", "Billie Eilish", 250, "Pop", 
                       "[\"Dreamy\", \"Emotional\", \"Reflective\"]", 120, 2L),
            createSong("Shape of You", "Ed Sheeran", 233, "Pop", 
                       "[\"Romantic\", \"Dance\", \"Upbeat\"]", 96, 3L),
            createSong("Perfect", "Ed Sheeran", 263, "Pop", 
                       "[\"Romantic\", \"Sentimental\", \"Love\"]", 95, 3L),
            createSong("24K Magic", "Bruno Mars", 225, "Funk", 
                       "[\"Funky\", \"Party\", \"Energetic\"]", 107, 4L),
            createSong("That's What I Like", "Bruno Mars", 206, "R&B", 
                       "[\"Romantic\", \"Smooth\", \"Chill\"]", 133, 4L),
            createSong("What a Wonderful World", "Louis Armstrong", 139, "Jazz", 
                       "[\"Happy\", \"Optimistic\", \"Classic\"]", 76, 5L),
            createSong("Summertime", "Ella Fitzgerald", 215, "Jazz", 
                       "[\"Relaxed\", \"Summer\", \"Nostalgic\"]", 82, 5L)
        );
        
        songRepository.saveAll(songs);
        log.info("初始化 {} 首歌曲数据", songs.size());
    }

    private Song createSong(String title, String artist, int duration, String genre, 
                           String moodTags, int bpm, Long albumId) {
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(title + " Album");
        song.setDuration(duration);
        song.setGenre(genre);
        song.setBpm(bpm);
        song.setAudioUrl("/audio/" + title.toLowerCase().replace(" ", "-") + ".mp3");
        song.setCoverImage("/covers/" + title.toLowerCase().replace(" ", "-") + ".jpg");
        song.setPlayCount((int)(Math.random() * 2000) + 500);
        song.setLikeCount((int)(Math.random() * 1000) + 200);
        return song;
    }

    private void initializePlaylists() {
        List<Playlist> playlists = Arrays.asList(
            createPlaylist("工作专注音乐", "适合工作学习时听的专注音乐", 2L, "WORK"),
            createPlaylist("运动能量歌单", "健身运动时的高能量音乐", 3L, "SPORT"),
            createPlaylist("放松爵士时光", "放松时听的经典爵士音乐", 5L, "RELAX"),
            createPlaylist("派对狂欢音乐", "派对聚会的热门音乐", 3L, "PARTY")
        );
        
        playlistRepository.saveAll(playlists);
        log.info("初始化 {} 个歌单数据", playlists.size());
    }

    private Playlist createPlaylist(String title, String description, Long creatorId, String sceneType) {
        Playlist playlist = new Playlist();
        playlist.setName(title);
        playlist.setDescription(description);
        playlist.setCoverImage("/covers/" + title.toLowerCase().replace(" ", "-") + ".jpg");
        playlist.setSongCount((int)(Math.random() * 10) + 5);
        playlist.setPlayCount((int)(Math.random() * 500) + 100);
        return playlist;
    }
}