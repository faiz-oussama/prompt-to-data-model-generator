package com.univade.ai_data_model.application.service;

import com.univade.ai_data_model.domain.model.ConversationContext;
import com.univade.ai_data_model.domain.model.DataModelGenerationResult;
import com.univade.ai_data_model.domain.model.PromptContext;

import java.util.List;
import java.util.Optional;

public interface DataModelGenerationService {
    DataModelGenerationResult generateDataModel(PromptContext context);
    DataModelGenerationResult generateDataModelWithMemory(PromptContext context);
    DataModelGenerationResult refineDataModel(String sessionId, String refinementInstructions);
    DataModelGenerationResult refineDataModelWithMemory(String conversationId, String refinementInstructions);
    DataModelGenerationResult continueConversation(String conversationId, String userInput);
    PromptContext buildContext(String userInput);
    PromptContext buildConversationContext(String userInput, boolean useMemory);
    Optional<ConversationContext> getConversationContext(String conversationId);
    void endConversation(String conversationId);
}