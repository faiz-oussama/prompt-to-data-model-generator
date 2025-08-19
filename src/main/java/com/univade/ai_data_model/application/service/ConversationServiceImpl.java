package com.univade.ai_data_model.application.service;

import com.univade.ai_data_model.domain.exception.ConversationNotFoundException;
import com.univade.ai_data_model.domain.model.ConversationContext;
import com.univade.ai_data_model.domain.model.ConversationStatus;
import com.univade.ai_data_model.domain.model.ConversationSummary;
import com.univade.ai_data_model.domain.repository.ConversationContextRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConversationServiceImpl implements ConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationServiceImpl.class);
    private final ConversationContextRepository conversationContextRepository;

    public ConversationServiceImpl(ConversationContextRepository conversationContextRepository) {
        this.conversationContextRepository = conversationContextRepository;
    }

    @Override
    public String startConversation(String userId, String sessionId) {
        ConversationContext context = ConversationContext.builder()
                .conversationId(UUID.randomUUID().toString())
                .userId(userId)
                .sessionId(sessionId)
                .status(ConversationStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .messageCount(0)
                .build();

        String conversationId = conversationContextRepository.save(context);
        logger.info("Started conversation with ID: {} for user: {}", conversationId, userId);
        return conversationId;
    }

    @Override
    public void endConversation(String conversationId) {
        conversationContextRepository.updateStatus(conversationId, ConversationStatus.ENDED.name());
        logger.info("Ended conversation with ID: {}", conversationId);
    }

    @Override
    public Optional<ConversationContext> getConversationContext(String conversationId) {
        return conversationContextRepository.findById(conversationId);
    }

    @Override
    public List<ConversationSummary> getUserConversations(String userId) {
        return conversationContextRepository.findByUserId(userId);
    }

    @Override
    public List<ConversationSummary> getSessionConversations(String sessionId) {
        return conversationContextRepository.findBySessionId(sessionId);
    }

    @Override
    public void incrementMessageCount(String conversationId) {
        try {
            conversationContextRepository.incrementMessageCount(conversationId);
        } catch (Exception e) {
            logger.error("Failed to increment message count for conversation: {}", conversationId, e);
            throw new ConversationNotFoundException("Conversation not found: " + conversationId);
        }
    }
}