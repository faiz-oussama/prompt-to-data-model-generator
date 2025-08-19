package com.univade.ai_data_model.domain.repository;

import com.univade.ai_data_model.domain.model.PromptContext;

import java.util.Optional;

public interface MemoryRepository {
    void save(String sessionId, PromptContext context);
    Optional<PromptContext> findBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);
    void deleteAllExpired();
}