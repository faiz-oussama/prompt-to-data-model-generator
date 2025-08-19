package com.univade.ai_data_model.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSummary {
    private String conversationId;
    private String userId;
    private String sessionId;
    private ConversationStatus status;
    private int messageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}