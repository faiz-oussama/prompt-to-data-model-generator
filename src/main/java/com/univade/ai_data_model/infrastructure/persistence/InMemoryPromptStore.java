package com.univade.ai_data_model.infrastructure.persistence;

import com.univade.ai_data_model.domain.model.PromptContext;
import com.univade.ai_data_model.domain.repository.MemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryPromptStore implements MemoryRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(InMemoryPromptStore.class);
    
    private final Map<String, PromptContext> contextMap = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> expirationMap = new ConcurrentHashMap<>();
    
    private static final long EXPIRATION_HOURS = 24;
    
    @Override
    public void save(String sessionId, PromptContext context) {
        contextMap.put(sessionId, context);
        expirationMap.put(sessionId, LocalDateTime.now().plusHours(EXPIRATION_HOURS));
        logger.debug("Saved context for session: {}", sessionId);
    }
    
    @Override
    public Optional<PromptContext> findBySessionId(String sessionId) {
        PromptContext context = contextMap.get(sessionId);
        if (context != null) {
            // Refresh expiration
            expirationMap.put(sessionId, LocalDateTime.now().plusHours(EXPIRATION_HOURS));
            return Optional.of(context);
        }
        return Optional.empty();
    }
    
    @Override
    public void deleteBySessionId(String sessionId) {
        contextMap.remove(sessionId);
        expirationMap.remove(sessionId);
        logger.debug("Deleted context for session: {}", sessionId);
    }
    
    @Override
    public void deleteAllExpired() {
        LocalDateTime now = LocalDateTime.now();
        expirationMap.entrySet().removeIf(entry -> {
            if (entry.getValue().isBefore(now)) {
                contextMap.remove(entry.getKey());
                logger.debug("Expired session removed: {}", entry.getKey());
                return true;
            }
            return false;
        });
    }
}