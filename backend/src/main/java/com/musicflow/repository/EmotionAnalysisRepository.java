package com.musicflow.repository;

import com.musicflow.entity.EmotionAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmotionAnalysisRepository extends JpaRepository<EmotionAnalysis, Long> {
}