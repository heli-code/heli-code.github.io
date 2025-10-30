package com.musicflow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    /**
     * 获取缓存值
     */
    public <T> Optional<T> get(String cacheName, Object key, Class<T> type) {
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                Cache.ValueWrapper valueWrapper = cache.get(key);
                if (valueWrapper != null) {
                    T value = type.cast(valueWrapper.get());
                    log.debug("缓存命中 - {}: {}", cacheName, key);
                    return Optional.of(value);
                }
            }
        } catch (Exception e) {
            log.warn("获取缓存失败 - {}: {}", cacheName, key, e);
        }
        return Optional.empty();
    }

    /**
     * 设置缓存值
     */
    public void put(String cacheName, Object key, Object value) {
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.put(key, value);
                log.debug("缓存设置 - {}: {}", cacheName, key);
            }
        } catch (Exception e) {
            log.warn("设置缓存失败 - {}: {}", cacheName, key, e);
        }
    }

    /**
     * 删除缓存值
     */
    public void evict(String cacheName, Object key) {
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.evict(key);
                log.debug("缓存删除 - {}: {}", cacheName, key);
            }
        } catch (Exception e) {
            log.warn("删除缓存失败 - {}: {}", cacheName, key, e);
        }
    }

    /**
     * 清空缓存区域
     */
    public void clear(String cacheName) {
        try {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.debug("缓存清空 - {}", cacheName);
            }
        } catch (Exception e) {
            log.warn("清空缓存失败 - {}", cacheName, e);
        }
    }

    /**
     * 歌曲相关缓存操作
     */
    @Cacheable(value = "songs", key = "#id")
    public Optional<Object> getSongById(Long id) {
        log.debug("查询歌曲详情，ID: {}", id);
        return Optional.empty(); // 实际实现中会调用歌曲服务
    }

    @CachePut(value = "songs", key = "#song.id")
    public Object updateSongCache(Object song) {
        log.debug("更新歌曲缓存: {}", song);
        return song;
    }

    @CacheEvict(value = "songs", key = "#id")
    public void evictSongCache(Long id) {
        log.debug("删除歌曲缓存: {}", id);
    }

    /**
     * 用户相关缓存操作
     */
    @Cacheable(value = "users", key = "#id")
    public Optional<Object> getUserById(Long id) {
        log.debug("查询用户详情，ID: {}", id);
        return Optional.empty(); // 实际实现中会调用用户服务
    }

    @Caching(
        put = {
            @CachePut(value = "users", key = "#user.id"),
            @CachePut(value = "user-profiles", key = "#user.id")
        }
    )
    public Object updateUserCache(Object user) {
        log.debug("更新用户缓存: {}", user);
        return user;
    }

    @Caching(
        evict = {
            @CacheEvict(value = "users", key = "#id"),
            @CacheEvict(value = "user-profiles", key = "#id")
        }
    )
    public void evictUserCache(Long id) {
        log.debug("删除用户缓存: {}", id);
    }

    /**
     * 播放列表相关缓存操作
     */
    @Cacheable(value = "playlists", key = "#id")
    public Optional<Object> getPlaylistById(Long id) {
        log.debug("查询播放列表，ID: {}", id);
        return Optional.empty(); // 实际实现中会调用播放列表服务
    }

    @Cacheable(value = "playlist-details", key = "#id")
    public Optional<Object> getPlaylistDetails(Long id) {
        log.debug("查询播放列表详情，ID: {}", id);
        return Optional.empty();
    }

    /**
     * 热门内容缓存操作
     */
    @Cacheable(value = "trending", key = "#type + '-' + #limit")
    public Optional<Object> getTrendingContent(String type, int limit) {
        log.debug("查询热门内容，类型: {}, 数量: {}", type, limit);
        return Optional.empty();
    }

    @Cacheable(value = "recommendations", key = "#userId + '-' + #type")
    public Optional<Object> getRecommendations(Long userId, String type) {
        log.debug("查询推荐内容，用户: {}, 类型: {}", userId, type);
        return Optional.empty();
    }

    /**
     * 批量缓存操作
     */
    public void batchEvictUserRelatedCaches(Long userId) {
        // 删除用户相关的所有缓存
        evictUserCache(userId);
        
        // 删除用户相关的播放列表缓存
        clear("playlists");
        clear("playlist-details");
        
        // 删除用户相关的推荐缓存
        evict("recommendations", userId + "-songs");
        evict("recommendations", userId + "-users");
        
        log.debug("批量删除用户相关缓存: {}", userId);
    }

    /**
     * 缓存统计信息
     */
    public void printCacheStats() {
        String[] cacheNames = {"songs", "users", "playlists", "trending", "recommendations"};
        
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null && cache.getNativeCache() instanceof com.github.benmanes.caffeine.cache.Cache) {
                com.github.benmanes.caffeine.cache.Cache nativeCache = 
                    (com.github.benmanes.caffeine.cache.Cache) cache.getNativeCache();
                log.info("缓存统计 - {}: 大小={}, 命中率={}", 
                    cacheName, 
                    nativeCache.estimatedSize(),
                    "N/A"); // Caffeine统计信息需要额外配置
            }
        }
    }
}