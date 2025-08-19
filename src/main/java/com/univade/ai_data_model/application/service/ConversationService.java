package com.univade.ai_data_model.application.service;

import com.univade.ai_data_model.domain.model.ConversationContext;
import com.univade.ai_data_model.domain.model.ConversationSummary;

import java.util.List;
import java.util.Optional;

public interface ConversationService {
    String startConversation(String userId, String sessionId);
    void endConversation(String conversationId);
    Optional<ConversationContext> getConversationContext(String conversationId);
    List<ConversationSummary> getUserConversations(String userId);
    List<ConversationSummary> getSessionConversations(String sessionId);
    void incrementMessageCount(String conversationId);
}