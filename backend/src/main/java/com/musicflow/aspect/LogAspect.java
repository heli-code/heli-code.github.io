package com.musicflow.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志切面 - 记录控制器方法调用日志
 */
@Slf4j
@Aspect
@Component
public class LogAspect {
    
    /**
     * 环绕通知：记录控制器方法调用日志
     */
    @Around("execution(* com.musicflow.controller..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.toString(joinPoint.getArgs());
        
        // 记录方法开始日志
        if (request != null) {
            log.info("请求开始 - URI: {} {} | 方法: {}.{} | 参数: {}", 
                    request.getMethod(), request.getRequestURI(), 
                    className, methodName, args);
        } else {
            log.info("方法开始 - {}.{} | 参数: {}", className, methodName, args);
        }
        
        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            // 记录方法结束日志
            log.info("请求完成 - {}.{} | 执行时间: {}ms", 
                    className, methodName, executionTime);
            
            return result;
            
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // 记录异常日志
            log.error("请求异常 - {}.{} | 执行时间: {}ms | 异常: {}", 
                    className, methodName, executionTime, e.getMessage(), e);
            
            throw e;
        }
    }
    
    /**
     * 环绕通知：记录服务层方法调用日志
     */
    @Around("execution(* com.musicflow.service..*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        // 记录方法开始日志
        log.debug("服务方法开始 - {}.{}", className, methodName);
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;
            
            // 如果执行时间超过阈值，记录警告
            if (executionTime > 1000) {
                log.warn("服务方法执行较慢 - {}.{} | 执行时间: {}ms", 
                        className, methodName, executionTime);
            }
            
            log.debug("服务方法完成 - {}.{} | 执行时间: {}ms", 
                    className, methodName, executionTime);
            
            return result;
            
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            log.error("服务方法异常 - {}.{} | 执行时间: {}ms | 异常: {}", 
                    className, methodName, executionTime, e.getMessage(), e);
            
            throw e;
        }
    }
}