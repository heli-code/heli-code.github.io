package com.musicflow.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置Redis缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 默认缓存配置
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // 默认缓存1小时
                .disableCachingNullValues() // 不缓存null值
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // 不同缓存区域的特定配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // 歌曲相关缓存配置
        cacheConfigurations.put("songs", defaultCacheConfig.entryTtl(Duration.ofHours(2)));
        cacheConfigurations.put("song-details", defaultCacheConfig.entryTtl(Duration.ofHours(4)));
        
        // 用户相关缓存配置
        cacheConfigurations.put("users", defaultCacheConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("user-profiles", defaultCacheConfig.entryTtl(Duration.ofHours(1)));
        
        // 播放列表缓存配置
        cacheConfigurations.put("playlists", defaultCacheConfig.entryTtl(Duration.ofHours(2)));
        cacheConfigurations.put("playlist-details", defaultCacheConfig.entryTtl(Duration.ofHours(3)));
        
        // 社交内容缓存配置
        cacheConfigurations.put("social-feed", defaultCacheConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("comments", defaultCacheConfig.entryTtl(Duration.ofHours(1)));
        
        // 热门内容缓存配置（缓存时间较短，保证数据新鲜度）
        cacheConfigurations.put("trending", defaultCacheConfig.entryTtl(Duration.ofMinutes(10)));
        cacheConfigurations.put("recommendations", defaultCacheConfig.entryTtl(Duration.ofMinutes(30)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    /**
     * 备用内存缓存管理器（当Redis不可用时）
     */
    @Bean
    public CacheManager fallbackCacheManager() {
        return new ConcurrentMapCacheManager("songs", "users", "playlists");
    }
}