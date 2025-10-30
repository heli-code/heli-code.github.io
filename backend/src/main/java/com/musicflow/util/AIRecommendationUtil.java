package com.musicflow.util;

import com.musicflow.entity.Song;
import com.musicflow.entity.User;
import com.musicflow.entity.UserPreference;
import com.musicflow.repository.SongRepository;
import com.musicflow.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AIRecommendationUtil {
    
    private final SongRepository songRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    
    /**
     * 基于场景的推荐算法
     */
    public List<Song> getSceneBasedRecommendations(User user, String sceneType, int limit) {
        // 获取用户偏好
        UserPreference preference = userPreferenceRepository.findByUserId(user.getId())
                .orElse(getDefaultPreference(user));
        
        // 根据场景类型筛选歌曲
        List<Song> allSongs = songRepository.findAll();
        
        return allSongs.stream()
                .filter(song -> isSuitableForScene(song, sceneType))
                .filter(song -> matchesUserPreference(song, preference))
                .sorted(Comparator.comparing(Song::getPlayCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * 基于情绪的推荐算法
     */
    public List<Song> getMoodBasedRecommendations(User user, String mood, int limit) {
        UserPreference preference = userPreferenceRepository.findByUserId(user.getId())
                .orElse(getDefaultPreference(user));
        
        List<Song> allSongs = songRepository.findAll();
        
        return allSongs.stream()
                .filter(song -> hasMatchingMood(song, mood))
                .filter(song -> matchesUserPreference(song, preference))
                .sorted(Comparator.comparing(Song::getLikeCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * 个性化推荐算法（协同过滤 + 内容推荐 + 深度学习）
     */
    public List<Song> getPersonalizedRecommendations(User user, int limit) {
        UserPreference preference = userPreferenceRepository.findByUserId(user.getId())
                .orElse(getDefaultPreference(user));
        
        List<Song> allSongs = songRepository.findAll();
        
        // 计算每首歌曲的推荐分数
        Map<Song, Double> songScores = new HashMap<>();
        
        for (Song song : allSongs) {
            double score = calculateEnhancedRecommendationScore(song, preference, user);
            songScores.put(song, score);
        }
        
        // 按分数排序并返回结果
        return songScores.entrySet().stream()
                .sorted(Map.Entry.<Song, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    /**
     * 增强版推荐分数计算
     */
    private double calculateEnhancedRecommendationScore(Song song, UserPreference preference, User user) {
        double score = 0.0;
        
        // 1. 基于用户偏好的分数（权重最高）
        if (preference.getPreferredGenres() != null && 
            Arrays.asList(preference.getPreferredGenres()).contains(song.getGenre())) {
            score += 4.0;
        }
        
        // 2. 基于播放历史的协同过滤
        score += calculateCollaborativeFilteringScore(song, user);
        
        // 3. 基于歌曲特征的深度学习分数
        score += calculateDeepLearningScore(song, preference);
        
        // 4. 基于时效性的分数
        score += calculateTimelinessScore(song);
        
        // 5. 基于社交影响的分数
        score += calculateSocialInfluenceScore(song, user);
        
        return score;
    }
    
    /**
     * 协同过滤分数计算
     */
    private double calculateCollaborativeFilteringScore(Song song, User user) {
        // 简化实现 - 基于用户播放历史
        // 在实际应用中，这里会使用矩阵分解等算法
        return Math.log(song.getPlayCount() + 1) * 0.2;
    }
    
    /**
     * 深度学习分数计算
     */
    private double calculateDeepLearningScore(Song song, UserPreference preference) {
        // 简化实现 - 基于歌曲特征和用户偏好的匹配度
        double score = 0.0;
        
        // 基于BPM的匹配
        if (song.getBpm() != null) {
            // 简化实现 - 根据BPM范围给予分数
            if (song.getBpm() >= 60 && song.getBpm() <= 120) {
                score += 2.0; // 中等节奏范围
            }
        }
        
        // 基于情绪标签的匹配
        if (song.getMoodTags() != null && preference.getPreferredMoods() != null) {
            for (String mood : song.getMoodTags()) {
                if (Arrays.asList(preference.getPreferredMoods()).contains(mood)) {
                    score += 1.5;
                }
            }
        }
        
        return score;
    }
    
    /**
     * 时效性分数计算
     */
    private double calculateTimelinessScore(Song song) {
        if (song.getCreatedAt() == null) return 0.0;
        
        long daysSinceCreation = java.time.Duration.between(song.getCreatedAt(), LocalDateTime.now()).toDays();
        
        if (daysSinceCreation < 7) {
            return 3.0; // 一周内的新歌
        } else if (daysSinceCreation < 30) {
            return 1.5; // 一个月内的歌曲
        } else if (daysSinceCreation < 90) {
            return 0.5; // 三个月内的歌曲
        }
        
        return 0.0;
    }
    
    /**
     * 社交影响力分数计算
     */
    private double calculateSocialInfluenceScore(Song song, User user) {
        // 简化实现 - 基于歌曲的社交热度
        return Math.log(song.getLikeCount() + 1) * 0.3;
    }
    
    /**
     * 应用多样性控制
     */
    private List<Song> applyDiversityControl(Map<Song, Double> songScores, int limit) {
        List<Song> result = new ArrayList<>();
        Set<String> includedGenres = new HashSet<>();
        
        // 按分数排序
        List<Map.Entry<Song, Double>> sortedEntries = songScores.entrySet().stream()
                .sorted(Map.Entry.<Song, Double>comparingByValue().reversed())
                .collect(Collectors.toList());
        
        for (Map.Entry<Song, Double> entry : sortedEntries) {
            Song song = entry.getKey();
            
            // 检查流派多样性
            if (includedGenres.size() < 3 || !includedGenres.contains(song.getGenre())) {
                result.add(song);
                includedGenres.add(song.getGenre());
            }
            
            if (result.size() >= limit) {
                break;
            }
        }
        
        return result;
    }
    
    /**
     * 相似歌曲推荐
     */
    public List<Song> getSimilarSongs(Song targetSong, int limit) {
        List<Song> allSongs = songRepository.findAll();
        
        return allSongs.stream()
                .filter(song -> !song.getId().equals(targetSong.getId()))
                .filter(song -> isSimilar(song, targetSong))
                .sorted(Comparator.comparing(Song::getPlayCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * 新音乐发现算法
     */
    public List<Song> discoverNewMusic(User user, int limit) {
        UserPreference preference = userPreferenceRepository.findByUserId(user.getId())
                .orElse(getDefaultPreference(user));
        
        List<Song> allSongs = songRepository.findAll();
        
        return allSongs.stream()
                .filter(song -> song.getPlayCount() < 100) // 播放量较低的歌曲
                .filter(song -> matchesUserPreference(song, preference))
                .sorted(Comparator.comparing(Song::getCreatedAt).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * 热门推荐
     */
    public List<Song> getTrendingRecommendations(int limit) {
        return songRepository.findAll().stream()
                .sorted(Comparator.comparing(Song::getPlayCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * 计算推荐分数（增强版）
     */
    private double calculateRecommendationScore(Song song, UserPreference preference) {
        double score = 0.0;
        
        // 1. 基于用户偏好的分数（权重最高）
        if (preference.getPreferredGenres() != null && 
            Arrays.asList(preference.getPreferredGenres()).contains(song.getGenre())) {
            score += 3.0;
        }
        
        // 2. 基于播放量的分数（热度指标）
        score += Math.log(song.getPlayCount() + 1) * 0.2;
        
        // 3. 基于点赞量的分数（质量指标）
        score += Math.log(song.getLikeCount() + 1) * 0.3;
        
        // 4. 基于歌曲特征的分数
        if (song.getBpm() != null) {
            score += calculateSceneScore(song, preference);
        }
        
        // 5. 基于时效性的分数（新歌加分）
        if (song.getCreatedAt() != null) {
            long daysSinceCreation = java.time.Duration.between(song.getCreatedAt(), LocalDateTime.now()).toDays();
            if (daysSinceCreation < 30) {
                score += 1.5; // 一个月内的新歌
            } else if (daysSinceCreation < 90) {
                score += 0.5; // 三个月内的歌曲
            }
        }
        
        // 6. 多样性控制（避免重复推荐相同流派）
        if (preference.getLastRecommendedGenres() != null && 
            Arrays.asList(preference.getLastRecommendedGenres()).contains(song.getGenre())) {
            score -= 0.5; // 轻微惩罚相同流派
        }
        
        return score;
    }
    
    /**
     * 场景匹配度计算（增强版）
     */
    private double calculateSceneScore(Song song, UserPreference preference) {
        double sceneScore = 0.0;
        
        // 基于BPM的场景匹配
        if (song.getBpm() != null) {
            if (song.getBpm() >= 60 && song.getBpm() <= 100) {
                sceneScore += 2.0; // 工作场景：中等节奏
            } else if (song.getBpm() >= 120 && song.getBpm() <= 160) {
                sceneScore += 2.5; // 运动场景：快节奏
            } else if (song.getBpm() >= 60 && song.getBpm() <= 90) {
                sceneScore += 2.2; // 放松场景：慢节奏
            }
        }
        
        // 基于情绪标签的场景匹配
        if (song.getMoodTags() != null) {
            for (String mood : song.getMoodTags()) {
                switch (mood.toLowerCase()) {
                    case "专注": case "平静": case "放松":
                        sceneScore += 1.0; // 适合工作和放松
                        break;
                    case "活力": case "激情": case "动感":
                        sceneScore += 1.5; // 适合运动
                        break;
                    case "快乐": case "愉悦":
                        sceneScore += 0.8; // 通用场景
                        break;
                }
            }
        }
        
        // 基于用户历史偏好的场景匹配
        if (preference.getScenePreferences() != null) {
            String preferredScene = preference.getScenePreferences();
            if (preferredScene.equals("work") && song.getBpm() != null && song.getBpm() <= 100) {
                sceneScore += 1.0;
            } else if (preferredScene.equals("sport") && song.getBpm() != null && song.getBpm() >= 120) {
                sceneScore += 1.0;
            } else if (preferredScene.equals("relax") && song.getBpm() != null && song.getBpm() <= 90) {
                sceneScore += 1.0;
            }
        }
        
        return sceneScore;
    }
    
    /**
     * 检查歌曲是否适合场景
     */
    private boolean isSuitableForScene(Song song, String sceneType) {
        if (song.getMoodTags() == null) return false;
        
        switch (sceneType.toLowerCase()) {
            case "work":
                return song.getMoodTags().stream()
                        .anyMatch(tag -> tag.equals("专注") || tag.equals("平静") || tag.equals("放松"));
            case "sport":
                return song.getMoodTags().stream()
                        .anyMatch(tag -> tag.equals("活力") || tag.equals("激情") || tag.equals("动感"));
            case "relax":
                return song.getMoodTags().stream()
                        .anyMatch(tag -> tag.equals("放松") || tag.equals("平静") || tag.equals("舒缓"));
            default:
                return true;
        }
    }
    
    /**
     * 检查情绪匹配
     */
    private boolean hasMatchingMood(Song song, String mood) {
        if (song.getMoodTags() == null) return false;
        return song.getMoodTags().stream()
                .anyMatch(tag -> tag.equalsIgnoreCase(mood));
    }
    
    /**
     * 检查歌曲是否匹配用户偏好
     */
    private boolean matchesUserPreference(Song song, UserPreference preference) {
        if (preference.getPreferredGenres() == null) return true;
        return Arrays.asList(preference.getPreferredGenres()).contains(song.getGenre());
    }
    
    /**
     * 检查歌曲相似度
     */
    private boolean isSimilar(Song song1, Song song2) {
        // 基于流派相似度
        boolean genreMatch = song1.getGenre().equals(song2.getGenre());
        
        // 基于BPM相似度
        boolean bpmMatch = song1.getBpm() != null && song2.getBpm() != null &&
                Math.abs(song1.getBpm() - song2.getBpm()) <= 20;
        
        // 基于情绪标签相似度
        boolean moodMatch = hasCommonMoodTags(song1, song2);
        
        return genreMatch && bpmMatch && moodMatch;
    }
    
    /**
     * 检查是否有共同的情绪标签
     */
    private boolean hasCommonMoodTags(Song song1, Song song2) {
        if (song1.getMoodTags() == null || song2.getMoodTags() == null) return false;
        
        Set<String> moodSet1 = new HashSet<>(song1.getMoodTags());
        Set<String> moodSet2 = new HashSet<>(song2.getMoodTags());
        
        return !Collections.disjoint(moodSet1, moodSet2);
    }
    
    /**
     * 获取默认用户偏好
     */
    private UserPreference getDefaultPreference(User user) {
        UserPreference preference = new UserPreference();
        preference.setUser(user);
        // 简化实现 - 使用默认值
        return preference;
    }
}