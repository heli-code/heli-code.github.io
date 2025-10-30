-- MusicFlow 数据库初始化数据
-- 基于AI智能推荐+音乐社交平台需求设计

-- 插入示例用户
INSERT INTO users (username, email, password_hash, avatar_url, nickname, bio, role, status, last_login_at, created_at, updated_at) VALUES
('admin', 'admin@musicflow.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOslX1M.n6F.p', '/avatars/admin.jpg', '管理员', '音乐平台管理员，热爱各种音乐类型', 'ADMIN', 'ACTIVE', NOW(), NOW(), NOW()),
('musiclover', 'musiclover@musicflow.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOslX1M.n6F.p', '/avatars/musiclover.jpg', '音乐爱好者', '喜欢流行音乐和轻音乐，经常在工作时听歌', 'USER', 'ACTIVE', NOW(), NOW(), NOW()),
('popfan', 'popfan@musicflow.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOslX1M.n6F.p', '/avatars/popfan.jpg', '流行音乐迷', '专注于流行音乐，喜欢发现新歌手', 'USER', 'ACTIVE', NOW(), NOW(), NOW()),
('rockstar', 'rockstar@musicflow.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOslX1M.n6F.p', '/avatars/rockstar.jpg', '摇滚之星', '摇滚音乐爱好者，喜欢现场音乐', 'VIP', 'ACTIVE', NOW(), NOW(), NOW()),
('jazzcat', 'jazzcat@musicflow.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOslX1M.n6F.p', '/avatars/jazzcat.jpg', '爵士猫', '爵士音乐专家，喜欢在放松时听爵士', 'USER', 'ACTIVE', NOW(), NOW(), NOW());

-- 插入用户偏好分析数据
INSERT INTO user_preferences (user_id, preferred_genres, preferred_artists, preferred_moods, scene_preferences, listening_habits, emotion_patterns, ai_personality_type, created_at, updated_at) VALUES
(2, '["Pop", "Light Music"]', '["The Weeknd", "Ed Sheeran", "Taylor Swift"]', '["Happy", "Relaxed"]', '{"work": 0.8, "relax": 0.6, "sport": 0.3}', '{"morning": 0.2, "afternoon": 0.6, "evening": 0.8}', '{"happy": 0.7, "focused": 0.6}', 'FRIENDLY', NOW(), NOW()),
(3, '["Pop", "Hip Hop"]', '["Billie Eilish", "Ariana Grande", "Drake"]', '["Energetic", "Excited"]', '{"party": 0.9, "sport": 0.7, "work": 0.4}', '{"afternoon": 0.5, "evening": 0.9}', '{"excited": 0.8, "energetic": 0.7}', 'ENERGETIC', NOW(), NOW()),
(4, '["Rock", "Metal"]', '["Linkin Park", "Metallica", "AC/DC"]', '["Energetic", "Focused"]', '{"sport": 0.9, "work": 0.5, "relax": 0.2}', '{"morning": 0.3, "afternoon": 0.7}', '{"energetic": 0.9, "focused": 0.8}', 'PROFESSIONAL', NOW(), NOW()),
(5, '["Jazz", "Blues"]', '["Louis Armstrong", "Ella Fitzgerald", "Miles Davis"]', '["Relaxed", "Happy"]', '{"relax": 0.9, "work": 0.3, "study": 0.6}', '{"evening": 0.8, "night": 0.9}', '{"relaxed": 0.9, "happy": 0.6}', 'FRIENDLY', NOW(), NOW());

-- 插入专辑数据
INSERT INTO albums (title, artist, cover_url, description, release_date, track_count, total_duration, created_at, updated_at) VALUES
('After Hours', 'The Weeknd', '/covers/after-hours.jpg', '2020年发行的第四张录音室专辑', '2020-03-20', 14, 3360, NOW(), NOW()),
('When We All Fall Asleep, Where Do We Go?', 'Billie Eilish', '/covers/when-we-all-fall-asleep.jpg', '2019年发行的首张录音室专辑', '2019-03-29', 14, 2580, NOW(), NOW()),
('÷', 'Ed Sheeran', '/covers/divide.jpg', '2017年发行的第三张录音室专辑', '2017-03-03', 16, 3840, NOW(), NOW()),
('24K Magic', 'Bruno Mars', '/covers/24k-magic.jpg', '2016年发行的第三张录音室专辑', '2016-11-18', 9, 2160, NOW(), NOW()),
('Jazz Classics', 'Various Artists', '/covers/jazz-classics.jpg', '经典爵士音乐合集', '2020-01-01', 20, 4800, NOW(), NOW());

-- 插入歌曲数据（支持AI推荐的特征）
INSERT INTO songs (title, artist, album_id, duration, genre, mood_tags, bpm, audio_url, cover_url, lyrics, release_date, play_count, like_count, share_count, is_public, created_at, updated_at) VALUES
('Blinding Lights', 'The Weeknd', 1, 200, 'Pop', '["Energetic", "Nostalgic", "Dance"]', 171, '/audio/blinding-lights.mp3', '/covers/blinding-lights.jpg', 'I\'m blinded by the lights...', '2020-03-20', 1500, 890, 120, TRUE, NOW(), NOW()),
('Save Your Tears', 'The Weeknd', 1, 215, 'Pop', '["Emotional", "Melancholic", "Dance"]', 118, '/audio/save-your-tears.mp3', '/covers/save-your-tears.jpg', 'I saw you dancing in a crowded room...', '2020-03-20', 1200, 750, 90, TRUE, NOW(), NOW()),
('Bad Guy', 'Billie Eilish', 2, 194, 'Pop', '["Dark", "Edgy", "Confident"]', 135, '/audio/bad-guy.mp3', '/covers/bad-guy.jpg', 'White shirt now red, my bloody nose...', '2019-03-29', 1100, 680, 85, TRUE, NOW(), NOW()),
('Everything I Wanted', 'Billie Eilish', 2, 250, 'Pop', '["Dreamy", "Emotional", "Reflective"]', 120, '/audio/everything-i-wanted.mp3', '/covers/everything-i-wanted.jpg', 'I had a dream I got everything I wanted...', '2019-11-13', 950, 620, 70, TRUE, NOW(), NOW()),
('Shape of You', 'Ed Sheeran', 3, 233, 'Pop', '["Romantic", "Dance", "Upbeat"]', 96, '/audio/shape-of-you.mp3', '/covers/shape-of-you.jpg', 'The club isn\'t the best place to find a lover...', '2017-01-06', 2000, 1200, 150, TRUE, NOW(), NOW()),
('Perfect', 'Ed Sheeran', 3, 263, 'Pop', '["Romantic", "Sentimental", "Love"]', 95, '/audio/perfect.mp3', '/covers/perfect.jpg', 'I found a love for me...', '2017-09-26', 1800, 1100, 130, TRUE, NOW(), NOW()),
('24K Magic', 'Bruno Mars', 4, 225, 'Funk', '["Funky", "Party", "Energetic"]', 107, '/audio/24k-magic.mp3', '/covers/24k-magic.jpg', 'Tonight I just want to take you higher...', '2016-10-07', 1600, 950, 110, TRUE, NOW(), NOW()),
('That\'s What I Like', 'Bruno Mars', 4, 206, 'R&B', '["Romantic", "Smooth", "Chill"]', 133, '/audio/thats-what-i-like.mp3', '/covers/thats-what-i-like.jpg', 'I got a condo in Manhattan...', '2017-01-30', 1400, 820, 95, TRUE, NOW(), NOW()),
('What a Wonderful World', 'Louis Armstrong', 5, 139, 'Jazz', '["Happy", "Optimistic", "Classic"]', 76, '/audio/what-a-wonderful-world.mp3', '/covers/what-a-wonderful-world.jpg', 'I see trees of green, red roses too...', '1967-01-01', 800, 450, 60, TRUE, NOW(), NOW()),
('Summertime', 'Ella Fitzgerald', 5, 215, 'Jazz', '["Relaxed", "Summer", "Nostalgic"]', 82, '/audio/summertime.mp3', '/covers/summertime.jpg', 'Summertime, and the livin\' is easy...', '1935-01-01', 700, 380, 45, TRUE, NOW(), NOW());

-- 插入歌曲特征数据（AI推荐算法使用）
INSERT INTO song_features (song_id, acousticness, danceability, energy, instrumentalness, liveness, speechiness, valence, tempo, key, mode, loudness, created_at) VALUES
(1, 0.012, 0.825, 0.786, 0.000, 0.089, 0.060, 0.518, 171.005, 1, 1, -5.934, NOW()),
(2, 0.008, 0.680, 0.652, 0.000, 0.356, 0.029, 0.389, 118.088, 7, 1, -6.647, NOW()),
(3, 0.256, 0.795, 0.524, 0.000, 0.112, 0.042, 0.127, 135.088, 1, 1, -7.127, NOW()),
(4, 0.089, 0.512, 0.245, 0.000, 0.098, 0.035, 0.178, 120.000, 6, 1, -8.234, NOW()),
(5, 0.083, 0.825, 0.652, 0.000, 0.080, 0.031, 0.931, 96.031, 0, 1, -3.183, NOW()),
(6, 0.267, 0.535, 0.324, 0.000, 0.092, 0.030, 0.402, 95.027, 4, 1, -6.407, NOW()),
(7, 0.015, 0.892, 0.784, 0.000, 0.365, 0.052, 0.815, 107.005, 11, 0, -4.771, NOW()),
(8, 0.024, 0.723, 0.586, 0.000, 0.092, 0.030, 0.689, 133.024, 9, 1, -5.934, NOW()),
(9, 0.945, 0.289, 0.245, 0.000, 0.126, 0.030, 0.945, 76.000, 7, 1, -14.236, NOW()),
(10, 0.823, 0.456, 0.289, 0.000, 0.112, 0.028, 0.723, 82.000, 3, 1, -12.847, NOW());

-- 插入歌单数据（UGC内容）
INSERT INTO playlists (title, description, cover_url, creator_id, is_public, song_count, total_duration, play_count, like_count, share_count, tags, scene_type, created_at, updated_at) VALUES
('工作专注音乐', '适合工作学习时听的专注音乐', '/covers/work-focus.jpg', 2, TRUE, 8, 1800, 350, 120, 45, '["Focus", "Work", "Study"]', 'WORK', NOW(), NOW()),
('运动能量歌单', '健身运动时的高能量音乐', '/covers/workout-energy.jpg', 3, TRUE, 6, 1350, 280, 95, 32, '["Workout", "Energy", "Sport"]', 'SPORT', NOW(), NOW()),
('放松爵士时光', '放松时听的经典爵士音乐', '/covers/relax-jazz.jpg', 5, TRUE, 5, 1200, 180, 65, 28, '["Relax", "Jazz", "Chill"]', 'RELAX', NOW(), NOW()),
('派对狂欢音乐', '派对聚会的热门音乐', '/covers/party-mix.jpg', 3, TRUE, 7, 1680, 420, 150, 52, '["Party", "Dance", "Fun"]', 'PARTY', NOW(), NOW());

-- 插入歌单歌曲关联数据
INSERT INTO playlist_songs (playlist_id, song_id, position, added_at) VALUES
(1, 4, 1, NOW()), (1, 6, 2, NOW()), (1, 10, 3, NOW()), (1, 9, 4, NOW()),
(2, 1, 1, NOW()), (2, 3, 2, NOW()), (2, 7, 3, NOW()), (2, 5, 4, NOW()),
(3, 9, 1, NOW()), (3, 10, 2, NOW()), (3, 4, 3, NOW()),
(4, 1, 1, NOW()), (4, 7, 2, NOW()), (4, 3, 3, NOW()), (4, 5, 4, NOW());

-- 插入用户行为记录（AI学习数据）
INSERT INTO user_behavior_logs (user_id, action_type, target_id, target_type, context_data, emotion_context, scene_context, timestamp) VALUES
(2, 'PLAY', 1, 'SONG', '{"position": 0, "device": "web"}', 'Focused', 'Work', NOW() - INTERVAL 1 HOUR),
(2, 'LIKE', 1, 'SONG', '{}', 'Happy', 'Work', NOW() - INTERVAL 55 MINUTE),
(2, 'PLAY', 4, 'SONG', '{"position": 30, "device": "web"}', 'Relaxed', 'Work', NOW() - INTERVAL 45 MINUTE),
(3, 'PLAY', 7, 'SONG', '{"position": 0, "device": "mobile"}', 'Energetic', 'Sport', NOW() - INTERVAL 2 HOUR),
(3, 'SHARE', 7, 'SONG', '{}', 'Excited', 'Sport', NOW() - INTERVAL 1 HOUR),
(4, 'PLAY', 3, 'SONG', '{"position": 0, "device": "web"}', 'Energetic', 'Work', NOW() - INTERVAL 3 HOUR),
(5, 'PLAY', 9, 'SONG', '{"position": 0, "device": "tablet"}', 'Relaxed', 'Relax', NOW() - INTERVAL 30 MINUTE);

-- 插入情绪分析记录
INSERT INTO emotion_analysis (user_id, emotion_type, confidence_score, trigger_context, analyzed_at) VALUES
(2, 'FOCUSED', 0.85, '{"activity": "working", "time_of_day": "afternoon"}', NOW() - INTERVAL 1 HOUR),
(3, 'ENERGETIC', 0.92, '{"activity": "exercising", "time_of_day": "evening"}', NOW() - INTERVAL 2 HOUR),
(5, 'RELAXED', 0.78, '{"activity": "relaxing", "time_of_day": "night"}', NOW() - INTERVAL 30 MINUTE);

-- 插入AI推荐结果
INSERT INTO ai_recommendations (user_id, recommendation_type, target_ids, algorithm_type, confidence_score, context_data, generated_at, expires_at) VALUES
(2, 'SONG', '[4, 6, 10]', 'content_based', 0.82, '{"scene": "work", "mood": "focused"}', NOW(), NOW() + INTERVAL 24 HOUR),
(3, 'PLAYLIST', '[2]', 'collaborative', 0.76, '{"scene": "sport", "mood": "energetic"}', NOW(), NOW() + INTERVAL 12 HOUR),
(5, 'SONG', '[9, 10]', 'hybrid', 0.89, '{"scene": "relax", "mood": "relaxed"}', NOW(), NOW() + INTERVAL 48 HOUR);

-- 插入AI对话历史
INSERT INTO ai_chat_history (user_id, user_message, ai_response, context_data, emotion_context, scene_context, chat_timestamp) VALUES
(2, '推荐一些适合工作的音乐', '根据您的偏好，为您推荐专注工作时的轻音乐和爵士乐', '{"current_song": 4}', 'Focused', 'Work', NOW() - INTERVAL 1 HOUR),
(3, '我想找些运动时听的歌', '为您找到了一些高能量的流行音乐和舞曲，适合运动时听', '{"current_activity": "exercising"}', 'Energetic', 'Sport', NOW() - INTERVAL 2 HOUR),
(5, '放松时听什么音乐好', '推荐一些经典的爵士乐和轻音乐，帮助您放松心情', '{}', 'Relaxed', 'Relax', NOW() - INTERVAL 30 MINUTE);

-- 插入用户关系数据
INSERT INTO user_relationships (follower_id, following_id, relationship_type, created_at) VALUES
(2, 3, 'FOLLOW', NOW()), (3, 2, 'FOLLOW', NOW()), (4, 3, 'FOLLOW', NOW()), (5, 2, 'FOLLOW', NOW());

-- 插入社交互动记录
INSERT INTO social_interactions (user_id, interaction_type, target_id, target_type, content, created_at) VALUES
(2, 'LIKE', 1, 'SONG', '这首歌太棒了！', NOW() - INTERVAL 1 DAY),
(3, 'SHARE', 7, 'SONG', '分享给大家！', NOW() - INTERVAL 2 DAY),
(4, 'LIKE', 3, 'SONG', '喜欢这种风格', NOW() - INTERVAL 3 DAY);

-- 插入评论数据
INSERT INTO comments (user_id, target_id, target_type, content, like_count, reply_count, parent_id, is_hot, created_at, updated_at) VALUES
(2, 1, 'SONG', '这首歌的旋律太棒了，每次听都很有感觉！', 15, 3, NULL, TRUE, NOW() - INTERVAL 1 DAY, NOW()),
(3, 1, 'SONG', '同意楼上的，特别是副歌部分很有感染力', 8, 1, 1, FALSE, NOW() - INTERVAL 20 HOUR, NOW()),
(4, 7, 'SONG', '运动时听这首歌特别带劲！', 12, 2, NULL, TRUE, NOW() - INTERVAL 2 DAY, NOW());

-- 插入成就系统数据
INSERT INTO achievements (achievement_name, description, icon_url, criteria_type, criteria_value, reward_points, is_hidden, created_at) VALUES
('音乐探索者', '收听100首不同的歌曲', '/icons/explorer.png', 'LISTEN_COUNT', 100, 100, FALSE, NOW()),
('社交达人', '获得50个关注者', '/icons/social-star.png', 'FOLLOWER_COUNT', 50, 150, FALSE, NOW()),
('歌单大师', '创建10个歌单', '/icons/playlist-master.png', 'PLAYLIST_COUNT', 10, 200, FALSE, NOW()),
('活跃用户', '连续登录30天', '/icons/active-user.png', 'DAYS_ACTIVE', 30, 250, FALSE, NOW());

-- 插入用户成就进度
INSERT INTO user_achievements (user_id, achievement_id, progress, completed_at, awarded_at) VALUES
(2, 1, 45, NULL, NULL), (2, 4, 15, NULL, NULL),
(3, 1, 78, NULL, NULL), (3, 2, 12, NULL, NULL),
(4, 1, 32, NULL, NULL), (5, 1, 23, NULL, NULL);

-- 插入场景模式配置
INSERT INTO scene_modes (mode_name, description, recommended_genres, tempo_range, energy_level, mood_tags, created_at) VALUES
('工作模式', '专注工作学习时的音乐推荐', '["Light Music", "Jazz", "Classical"]', '{"min": 60, "max": 100}', 'MEDIUM', '["Focused", "Calm"]', NOW()),
('运动模式', '健身运动时的高能量音乐', '["Pop", "Rock", "Hip Hop"]', '{"min": 120, "max": 160}', 'HIGH', '["Energetic", "Motivated"]', NOW()),
('放松模式', '休息放松时的舒缓音乐', '["Jazz", "Ambient", "Acoustic"]', '{"min": 60, "max": 90}', 'LOW', '["Relaxed", "Peaceful"]', NOW()),
('派对模式', '聚会派对的欢乐音乐', '["Dance", "Pop", "Funk"]', '{"min": 100, "max": 140}', 'HIGH', '["Happy", "Excited"]', NOW());

-- 插入热门内容缓存
INSERT INTO hot_content_cache (content_type, content_id, popularity_score, ranking_position, calculated_at, expires_at) VALUES
('SONG', 1, 0.892, 1, NOW(), NOW() + INTERVAL 24 HOUR),
('SONG', 5, 0.845, 2, NOW(), NOW() + INTERVAL 24 HOUR),
('SONG', 7, 0.812, 3, NOW(), NOW() + INTERVAL 24 HOUR),
('PLAYLIST', 2, 0.765, 1, NOW(), NOW() + INTERVAL 24 HOUR);

-- 插入用户画像统计
INSERT INTO user_profile_stats (user_id, total_listening_time, favorite_genres, active_days, last_active, created_at, updated_at) VALUES
(2, 86400, '["Pop", "Light Music"]', 15, NOW(), NOW(), NOW()),
(3, 129600, '["Pop", "Rock"]', 23, NOW(), NOW(), NOW()),
(4, 57600, '["Rock", "Metal"]', 8, NOW(), NOW(), NOW()),
(5, 43200, '["Jazz", "Blues"]', 12, NOW(), NOW(), NOW());