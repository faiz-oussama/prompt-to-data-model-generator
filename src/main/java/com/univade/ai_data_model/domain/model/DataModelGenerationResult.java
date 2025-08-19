package com.univade.ai_data_model.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class DataModelGenerationResult {
    private String sessionId;
    private String conversationId;
    private DataModelMetadata metadata;
    private String dataModelJson;
    private String status;
    private String errorMessage;
    private LocalDateTime generatedAt;
    private boolean isNewConversation;
    private String id = UUID.randomUUID().toString();

    public DataModelGenerationResult() {
        this(UUID.randomUUID().toString());
    }

    public DataModelGenerationResult(String sessionId) {
        this.sessionId = sessionId;
        this.generatedAt = LocalDateTime.now();
    }
    
    public DataModelGenerationResult(String sessionId, String conversationId, DataModelMetadata metadata,
                                     String dataModelJson, String status, String errorMessage, 
                                     LocalDateTime generatedAt, boolean isNewConversation) {
        this.sessionId = sessionId;
        this.conversationId = conversationId;
        this.metadata = metadata;
        this.dataModelJson = dataModelJson;
        this.status = status;
        this.errorMessage = errorMessage;
        this.generatedAt = generatedAt;
        this.isNewConversation = isNewConversation;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public DataModelMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(DataModelMetadata metadata) {
        this.metadata = metadata;
    }

    public String getDataModelJson() {
        return dataModelJson;
    }

    public void setDataModelJson(String dataModelJson) {
        this.dataModelJson = dataModelJson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public boolean isNewConversation() {
        return isNewConversation;
    }

    public void setNewConversation(boolean isNewConversation) {
        this.isNewConversation = isNewConversation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}