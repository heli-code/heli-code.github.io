package com.musicflow.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

/**
 * 日志拦截器 - 记录请求和响应日志
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {
    
    private static final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, 
                           Object handler) throws Exception {
        
        long startTime = System.currentTimeMillis();
        startTimeThreadLocal.set(startTime);
        
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIP = getClientIP(request);
        String userAgent = request.getHeader("User-Agent");
        
        // 记录请求开始日志
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("请求开始 - ");
        logMessage.append("IP: ").append(clientIP).append(" | ");
        logMessage.append("方法: ").append(method).append(" | ");
        logMessage.append("URI: ").append(uri);
        
        if (queryString != null) {
            logMessage.append("?").append(queryString);
        }
        
        logMessage.append(" | UA: ").append(userAgent);
        
        // 记录请求头（调试级别）
        if (log.isDebugEnabled()) {
            log.debug("请求头信息:");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.debug("  {}: {}", headerName, headerValue);
            }
        }
        
        log.info(logMessage.toString());
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, 
                               HttpServletResponse response, 
                               Object handler, 
                               Exception ex) throws Exception {
        
        Long startTime = startTimeThreadLocal.get();
        if (startTime == null) {
            return;
        }
        
        long executionTime = System.currentTimeMillis() - startTime;
        int status = response.getStatus();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIP = getClientIP(request);
        
        // 构建日志消息
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("请求完成 - ");
        logMessage.append("IP: ").append(clientIP).append(" | ");
        logMessage.append("方法: ").append(method).append(" | ");
        logMessage.append("URI: ").append(uri).append(" | ");
        logMessage.append("状态: ").append(status).append(" | ");
        logMessage.append("耗时: ").append(executionTime).append("ms");
        
        if (ex != null) {
            logMessage.append(" | 异常: ").append(ex.getMessage());
            log.error(logMessage.toString(), ex);
        } else {
            // 根据状态码和耗时决定日志级别
            if (status >= 400) {
                log.warn(logMessage.toString());
            } else if (executionTime > 1000) {
                log.warn("慢请求检测 - {}", logMessage.toString());
            } else {
                log.info(logMessage.toString());
            }
        }
        
        // 清理ThreadLocal
        startTimeThreadLocal.remove();
    }
    
    /**
     * 获取客户端真实IP地址
     */
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 对于多个代理的情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }
    
    /**
     * 记录响应信息（调试级别）
     */
    private void logResponseInfo(HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("响应头信息:");
            response.getHeaderNames().forEach(headerName -> {
                String headerValue = response.getHeader(headerName);
                log.debug("  {}: {}", headerName, headerValue);
            });
        }
    }
}