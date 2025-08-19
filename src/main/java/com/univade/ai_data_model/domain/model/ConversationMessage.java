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
public class ConversationMessage {
    private String conversationId;
    private String content;
    private MessageType type;
    private LocalDateTime timestamp;

    public static ConversationMessage userMessage(String conversationId, String content) {
        return ConversationMessage.builder()
                .conversationId(conversationId)
                .content(content)
                .type(MessageType.USER)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ConversationMessage assistantMessage(String conversationId, String content) {
        return ConversationMessage.builder()
                .conversationId(conversationId)
                .content(content)
                .type(MessageType.ASSISTANT)
                .timestamp(LocalDateTime.now())
                .build();
    }
}