package com.musicflow.service;

import com.musicflow.entity.EmotionAnalysis;
import com.musicflow.repository.EmotionAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmotionAnalysisService {
    
    private final EmotionAnalysisRepository emotionAnalysisRepository;
    
    public EmotionAnalysis analyzeEmotion(String text) {
        // 简化的情感分析实现
        EmotionAnalysis analysis = EmotionAnalysis.builder()
                .text(text)
                .emotionType(EmotionAnalysis.EmotionType.RELAXED) // 使用有效的枚举值
                .confidenceScore(new java.math.BigDecimal("0.5"))
                .build();
        
        return emotionAnalysisRepository.save(analysis);
    }
}