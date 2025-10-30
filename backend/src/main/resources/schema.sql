-- MusicFlow 数据库架构
-- 基于AI智能推荐+音乐社交平台需求设计

-- 用户基础信息表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(500),
    nickname VARCHAR(50),
    bio TEXT,
    role ENUM('USER', 'ADMIN', 'VIP') DEFAULT 'USER',
    status ENUM('ACTIVE', 'INACTIVE', 'BANNED') DEFAULT 'ACTIVE',
    last_login_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_created_at (created_at)
);

-- 用户偏好分析表（支持AI推荐）
CREATE TABLE IF NOT EXISTS user_preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    preferred_genres JSON, -- 偏好的音乐类型
    preferred_artists JSON, -- 偏好的艺术家
    preferred_moods JSON, -- 偏好的情绪类型
    scene_preferences JSON, -- 场景偏好（工作、运动、放松等）
    listening_habits JSON, -- 听歌习惯（时间段、时长等）
    emotion_patterns JSON, -- 情绪模式分析
    ai_personality_type ENUM('FRIENDLY', 'PROFESSIONAL', 'ENERGETIC') DEFAULT 'FRIENDLY',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
);

-- 歌曲基础信息表
CREATE TABLE IF NOT EXISTS songs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    artist VARCHAR(100) NOT NULL,
    album_id BIGINT,
    duration INT NOT NULL, -- 时长（秒）
    genre VARCHAR(50), -- 音乐类型
    mood_tags JSON, -- 情绪标签
    bpm INT, -- 节拍数
    audio_url VARCHAR(500) NOT NULL,
    cover_url VARCHAR(500),
    lyrics TEXT,
    release_date DATE,
    play_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    share_count INT DEFAULT 0,
    is_public BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_artist (artist),
    INDEX idx_genre (genre),
    INDEX idx_release_date (release_date)
);

-- 歌曲特征表（支持AI推荐算法）
CREATE TABLE IF NOT EXISTS song_features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    song_id BIGINT NOT NULL,
    acousticness DECIMAL(5,4), -- 原声度
    danceability DECIMAL(5,4), -- 可舞性
    energy DECIMAL(5,4), -- 能量
    instrumentalness DECIMAL(5,4), -- 器乐性
    liveness DECIMAL(5,4), -- 现场感
    speechiness DECIMAL(5,4), -- 语音度
    valence DECIMAL(5,4), -- 愉悦度
    tempo DECIMAL(6,2), -- 速度
    key INT, -- 调性
    mode INT, -- 调式
    loudness DECIMAL(5,2), -- 响度
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE,
    INDEX idx_song_id (song_id),
    INDEX idx_tempo (tempo),
    INDEX idx_energy (energy)
);

-- 专辑表
CREATE TABLE IF NOT EXISTS albums (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    artist VARCHAR(100) NOT NULL,
    cover_url VARCHAR(500),
    description TEXT,
    release_date DATE,
    track_count INT DEFAULT 0,
    total_duration INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_title (title),
    INDEX idx_artist (artist)
);

-- 歌单表（支持UGC内容）
CREATE TABLE IF NOT EXISTS playlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    cover_url VARCHAR(500),
    creator_id BIGINT NOT NULL,
    is_public BOOLEAN DEFAULT TRUE,
    song_count INT DEFAULT 0,
    total_duration INT DEFAULT 0,
    play_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    share_count INT DEFAULT 0,
    tags JSON, -- 标签分类
    scene_type ENUM('WORK', 'SPORT', 'RELAX', 'STUDY', 'PARTY') DEFAULT 'RELAX',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_creator_id (creator_id),
    INDEX idx_scene_type (scene_type),
    INDEX idx_created_at (created_at)
);

-- 歌单歌曲关联表
CREATE TABLE IF NOT EXISTS playlist_songs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    playlist_id BIGINT NOT NULL,
    song_id BIGINT NOT NULL,
    position INT NOT NULL, -- 在歌单中的位置
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (playlist_id) REFERENCES playlists(id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE,
    UNIQUE KEY uk_playlist_song (playlist_id, song_id),
    INDEX idx_playlist_id (playlist_id),
    INDEX idx_song_id (song_id)
);

-- 用户行为记录表（用于AI学习）
CREATE TABLE IF NOT EXISTS user_behavior_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    action_type ENUM('PLAY', 'PAUSE', 'SKIP', 'LIKE', 'SHARE', 'DOWNLOAD', 'COMMENT', 'SEARCH') NOT NULL,
    target_id BIGINT, -- 目标ID（歌曲、歌单等）
    target_type ENUM('SONG', 'PLAYLIST', 'ALBUM', 'USER') NOT NULL,
    context_data JSON, -- 上下文数据（播放位置、设备信息等）
    emotion_context VARCHAR(50), -- 情绪上下文
    scene_context VARCHAR(50), -- 场景上下文
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_action_type (action_type),
    INDEX idx_timestamp (timestamp),
    INDEX idx_user_action (user_id, action_type)
);

-- 情绪分析记录表
CREATE TABLE IF NOT EXISTS emotion_analysis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    emotion_type ENUM('HAPPY', 'SAD', 'EXCITED', 'RELAXED', 'ENERGETIC', 'FOCUSED') NOT NULL,
    confidence_score DECIMAL(4,3), -- 置信度
    trigger_context JSON, -- 触发上下文
    analyzed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_emotion_type (emotion_type),
    INDEX idx_analyzed_at (analyzed_at)
);

-- AI推荐结果表
CREATE TABLE IF NOT EXISTS ai_recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    recommendation_type ENUM('SONG', 'PLAYLIST', 'ARTIST', 'GENRE') NOT NULL,
    target_ids JSON NOT NULL, -- 推荐目标ID列表
    algorithm_type VARCHAR(50), -- 推荐算法类型
    confidence_score DECIMAL(4,3), -- 推荐置信度
    context_data JSON, -- 推荐上下文
    generated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME, -- 过期时间
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_recommendation_type (recommendation_type),
    INDEX idx_generated_at (generated_at)
);

-- AI对话历史表
CREATE TABLE IF NOT EXISTS ai_chat_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_message TEXT NOT NULL,
    ai_response TEXT NOT NULL,
    context_data JSON, -- 对话上下文
    emotion_context VARCHAR(50), -- 情绪上下文
    scene_context VARCHAR(50), -- 场景上下文
    chat_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_chat_timestamp (chat_timestamp)
);

-- 用户关系表（关注/粉丝）
CREATE TABLE IF NOT EXISTS user_relationships (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    relationship_type ENUM('FOLLOW', 'BLOCK') DEFAULT 'FOLLOW',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_follower_following (follower_id, following_id),
    INDEX idx_follower_id (follower_id),
    INDEX idx_following_id (following_id)
);

-- 社交互动记录表
CREATE TABLE IF NOT EXISTS social_interactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    interaction_type ENUM('LIKE', 'SHARE', 'COMMENT', 'REPOST') NOT NULL,
    target_id BIGINT NOT NULL,
    target_type ENUM('SONG', 'PLAYLIST', 'COMMENT', 'USER') NOT NULL,
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_interaction_type (interaction_type),
    INDEX idx_target (target_id, target_type)
);

-- 评论表
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    target_type ENUM('SONG', 'PLAYLIST', 'ALBUM') NOT NULL,
    content TEXT NOT NULL,
    like_count INT DEFAULT 0,
    reply_count INT DEFAULT 0,
    parent_id BIGINT, -- 父评论ID
    is_hot BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_target (target_id, target_type),
    INDEX idx_created_at (created_at)
);

-- 成就系统表
CREATE TABLE IF NOT EXISTS achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    achievement_name VARCHAR(100) NOT NULL,
    description TEXT,
    icon_url VARCHAR(500),
    criteria_type ENUM('LISTEN_COUNT', 'PLAYLIST_COUNT', 'FOLLOWER_COUNT', 'DAYS_ACTIVE') NOT NULL,
    criteria_value INT NOT NULL,
    reward_points INT DEFAULT 0,
    is_hidden BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_criteria_type (criteria_type)
);

-- 用户成就进度表
CREATE TABLE IF NOT EXISTS user_achievements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    achievement_id BIGINT NOT NULL,
    progress INT DEFAULT 0,
    completed_at DATETIME,
    awarded_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (achievement_id) REFERENCES achievements(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_achievement (user_id, achievement_id),
    INDEX idx_user_id (user_id)
);

-- 场景模式配置表
CREATE TABLE IF NOT EXISTS scene_modes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mode_name VARCHAR(50) NOT NULL,
    description TEXT,
    recommended_genres JSON,
    tempo_range JSON, -- {min: 60, max: 120}
    energy_level ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM',
    mood_tags JSON,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_mode_name (mode_name)
);

-- 热门内容缓存表
CREATE TABLE IF NOT EXISTS hot_content_cache (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_type ENUM('SONG', 'PLAYLIST', 'ALBUM', 'ARTIST') NOT NULL,
    content_id BIGINT NOT NULL,
    popularity_score DECIMAL(8,4) NOT NULL,
    ranking_position INT,
    calculated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NOT NULL,
    INDEX idx_content_type (content_type),
    INDEX idx_popularity_score (popularity_score),
    INDEX idx_expires_at (expires_at)
);

-- 用户画像统计表
CREATE TABLE IF NOT EXISTS user_profile_stats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_listening_time INT DEFAULT 0, -- 总听歌时长（秒）
    favorite_genres JSON,
    active_days INT DEFAULT 0,
    last_active DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_last_active (last_active)
);