package com.univade.ai_data_model.domain.repository;

import com.univade.ai_data_model.domain.model.DataModelGenerationResult;

import java.util.List;
import java.util.Optional;

public interface DataModelRepository {
    void save(DataModelGenerationResult result);
    Optional<DataModelGenerationResult> findById(String id);
    List<DataModelGenerationResult> findBySessionId(String sessionId);
    List<DataModelGenerationResult> findByConversationId(String conversationId);
    void deleteById(String id);
}