package com.musicflow.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 性能监控切面 - 监控方法执行性能
 */
@Slf4j
@Aspect
@Component
public class PerformanceAspect {
    
    private final Map<String, MethodStats> methodStatsMap = new ConcurrentHashMap<>();
    private static final long SLOW_METHOD_THRESHOLD = 500; // 慢方法阈值（毫秒）
    
    /**
     * 方法统计信息
     */
    private static class MethodStats {
        AtomicLong callCount = new AtomicLong(0);
        AtomicLong totalTime = new AtomicLong(0);
        AtomicLong slowCount = new AtomicLong(0);
        
        void addExecutionTime(long executionTime) {
            callCount.incrementAndGet();
            totalTime.addAndGet(executionTime);
            if (executionTime > SLOW_METHOD_THRESHOLD) {
                slowCount.incrementAndGet();
            }
        }
        
        long getAverageTime() {
            return callCount.get() == 0 ? 0 : totalTime.get() / callCount.get();
        }
    }
    
    /**
     * 环绕通知：监控服务层方法性能
     */
    @Around("execution(* com.musicflow.service..*(..))")
    public Object monitorServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodKey = getMethodKey(joinPoint);
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            // 记录方法执行时间
            recordMethodExecution(methodKey, executionTime);
            
            // 如果执行时间超过阈值，记录警告
            if (executionTime > SLOW_METHOD_THRESHOLD) {
                log.warn("慢方法检测 - {} | 执行时间: {}ms", methodKey, executionTime);
            }
            
            return result;
            
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            recordMethodExecution(methodKey, executionTime);
            throw e;
        }
    }
    
    /**
     * 环绕通知：监控控制器方法性能
     */
    @Around("execution(* com.musicflow.controller..*(..))")
    public Object monitorControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodKey = getMethodKey(joinPoint);
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            recordMethodExecution(methodKey, executionTime);
            
            if (executionTime > SLOW_METHOD_THRESHOLD) {
                log.warn("慢接口检测 - {} | 执行时间: {}ms", methodKey, executionTime);
            }
            
            return result;
            
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            recordMethodExecution(methodKey, executionTime);
            throw e;
        }
    }
    
    /**
     * 获取方法唯一标识
     */
    private String getMethodKey(ProceedingJoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        return className + "." + methodName;
    }
    
    /**
     * 记录方法执行统计
     */
    private void recordMethodExecution(String methodKey, long executionTime) {
        MethodStats stats = methodStatsMap.computeIfAbsent(methodKey, k -> new MethodStats());
        stats.addExecutionTime(executionTime);
        
        // 每100次调用输出一次统计信息
        if (stats.callCount.get() % 100 == 0) {
            log.info("方法性能统计 - {} | 调用次数: {} | 平均时间: {}ms | 慢调用次数: {}", 
                    methodKey, stats.callCount.get(), stats.getAverageTime(), stats.slowCount.get());
        }
    }
    
    /**
     * 获取所有方法的性能统计
     */
    public Map<String, Map<String, Object>> getPerformanceStats() {
        Map<String, Map<String, Object>> stats = new HashMap<>();
        
        methodStatsMap.forEach((methodKey, methodStats) -> {
            Map<String, Object> methodInfo = new HashMap<>();
            methodInfo.put("callCount", methodStats.callCount.get());
            methodInfo.put("totalTime", methodStats.totalTime.get());
            methodInfo.put("averageTime", methodStats.getAverageTime());
            methodInfo.put("slowCount", methodStats.slowCount.get());
            
            stats.put(methodKey, methodInfo);
        });
        
        return stats;
    }
    
    /**
     * 重置性能统计
     */
    public void resetStats() {
        methodStatsMap.clear();
        log.info("性能统计已重置");
    }
}