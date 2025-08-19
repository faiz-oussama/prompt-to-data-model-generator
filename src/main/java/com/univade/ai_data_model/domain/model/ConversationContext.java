package com.univade.ai_data_model.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationContext {
    private String conversationId;
    private String sessionId;
    private String userId;
    private ConversationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int messageCount;
    
    @Builder.Default
    private List<ConversationMessage> messages = new ArrayList<>();

    public void addMessage(ConversationMessage message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }
}