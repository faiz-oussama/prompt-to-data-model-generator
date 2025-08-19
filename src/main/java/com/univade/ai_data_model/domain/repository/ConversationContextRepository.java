package com.univade.ai_data_model.domain.repository;

import com.univade.ai_data_model.domain.model.ConversationContext;
import com.univade.ai_data_model.domain.model.ConversationSummary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationContextRepository {
    String save(ConversationContext context);
    Optional<ConversationContext> findById(String conversationId);
    List<ConversationSummary> findByUserId(String userId);
    List<ConversationSummary> findBySessionId(String sessionId);
    void deleteById(String conversationId);
    void deleteAllExpired();
    void incrementMessageCount(String conversationId);
    void updateStatus(String conversationId, String status);
}