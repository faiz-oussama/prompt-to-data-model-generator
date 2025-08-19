package com.univade.ai_data_model.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PromptContext {
    private String sessionId;
    private String conversationId;
    private String userInput;
    private boolean useConversationMemory;
    private List<ConversationMessage> messageHistory;

    public PromptContext() {
        this.sessionId = UUID.randomUUID().toString();
        this.messageHistory = new ArrayList<>();
    }

    public PromptContext(String sessionId, String userInput) {
        this.sessionId = sessionId;
        this.userInput = userInput;
        this.messageHistory = new ArrayList<>();
    }

    public PromptContext(String sessionId, String conversationId, String userInput) {
        this.sessionId = sessionId;
        this.conversationId = conversationId;
        this.userInput = userInput;
        this.messageHistory = new ArrayList<>();
    }

    public boolean shouldUseConversationMemory() {
        return useConversationMemory && conversationId != null && !conversationId.isEmpty();
    }

    public void addToHistory(ConversationMessage message) {
        if (messageHistory == null) {
            messageHistory = new ArrayList<>();
        }
        messageHistory.add(message);
    }
}