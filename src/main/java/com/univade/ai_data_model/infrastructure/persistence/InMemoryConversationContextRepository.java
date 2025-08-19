package com.univade.ai_data_model.infrastructure.persistence;

import com.univade.ai_data_model.domain.model.ConversationContext;
import com.univade.ai_data_model.domain.model.ConversationStatus;
import com.univade.ai_data_model.domain.model.ConversationSummary;
import com.univade.ai_data_model.domain.repository.ConversationContextRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryConversationContextRepository implements ConversationContextRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryConversationContextRepository.class);

    private final ConcurrentMap<String, ConversationContext> conversationIdToContext = new ConcurrentHashMap<>();

    @Override
    public String save(ConversationContext context) {
        String conversationId = context.getConversationId();
        if (conversationId == null || conversationId.isBlank()) {
            conversationId = UUID.randomUUID().toString();
            context.setConversationId(conversationId);
        }
        if (context.getCreatedAt() == null) {
            context.setCreatedAt(LocalDateTime.now());
        }
        context.setUpdatedAt(LocalDateTime.now());
        conversationIdToContext.put(conversationId, context);
        logger.debug("Saved conversation context: {}", conversationId);
        return conversationId;
    }

    @Override
    public Optional<ConversationContext> findById(String conversationId) {
        return Optional.ofNullable(conversationIdToContext.get(conversationId));
    }

    @Override
    public List<ConversationSummary> findByUserId(String userId) {
        List<ConversationSummary> summaries = new ArrayList<>();
        for (ConversationContext context : conversationIdToContext.values()) {
            if (userId != null && userId.equals(context.getUserId())) {
                summaries.add(toSummary(context));
            }
        }
        return summaries;
    }

    @Override
    public List<ConversationSummary> findBySessionId(String sessionId) {
        List<ConversationSummary> summaries = new ArrayList<>();
        for (ConversationContext context : conversationIdToContext.values()) {
            if (sessionId != null && sessionId.equals(context.getSessionId())) {
                summaries.add(toSummary(context));
            }
        }
        return summaries;
    }

    @Override
    public void deleteById(String conversationId) {
        conversationIdToContext.remove(conversationId);
        logger.debug("Deleted conversation context: {}", conversationId);
    }

    @Override
    public void deleteAllExpired() {
        conversationIdToContext.values().removeIf(ctx -> ctx.getStatus() == ConversationStatus.EXPIRED);
    }

    @Override
    public void incrementMessageCount(String conversationId) {
        conversationIdToContext.computeIfPresent(conversationId, (id, ctx) -> {
            ctx.setMessageCount(ctx.getMessageCount() + 1);
            ctx.setUpdatedAt(LocalDateTime.now());
            return ctx;
        });
    }

    @Override
    public void updateStatus(String conversationId, String status) {
        conversationIdToContext.computeIfPresent(conversationId, (id, ctx) -> {
            try {
                ConversationStatus newStatus = ConversationStatus.valueOf(status);
                ctx.setStatus(newStatus);
            } catch (IllegalArgumentException ignored) {
                // keep existing status if invalid value provided
            }
            ctx.setUpdatedAt(LocalDateTime.now());
            return ctx;
        });
    }

    private ConversationSummary toSummary(ConversationContext context) {
        return ConversationSummary.builder()
                .conversationId(context.getConversationId())
                .userId(context.getUserId())
                .sessionId(context.getSessionId())
                .status(context.getStatus())
                .messageCount(context.getMessageCount())
                .createdAt(context.getCreatedAt())
                .updatedAt(context.getUpdatedAt())
                .build();
    }
}



