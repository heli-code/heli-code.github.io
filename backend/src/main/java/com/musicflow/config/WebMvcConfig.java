package com.musicflow.config;

import com.musicflow.interceptor.AuthInterceptor;
import com.musicflow.interceptor.LogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    
    private final AuthInterceptor authInterceptor;
    private final LogInterceptor logInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加日志拦截器（所有请求）
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .order(1);
        
        // 添加认证拦截器（排除公开接口）
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/songs",
                        "/api/songs/search",
                        "/api/songs/popular",
                        "/api/songs/trending",
                        "/api/public/**"
                )
                .order(2);
    }
}