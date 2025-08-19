package com.univade.ai_data_model.application.usecase;

import com.univade.ai_data_model.application.service.DataModelGenerationService;
import com.univade.ai_data_model.domain.model.DataModelGenerationResult;
import com.univade.ai_data_model.domain.model.PromptContext;
import org.springframework.stereotype.Service;

@Service
public class GenerateDataModelUseCase {
    
    private final DataModelGenerationService dataModelGenerationService;
    
    public GenerateDataModelUseCase(DataModelGenerationService dataModelGenerationService) {
        this.dataModelGenerationService = dataModelGenerationService;
    }
    
    public DataModelGenerationResult execute(String userInput) {
        PromptContext context = dataModelGenerationService.buildContext(userInput);
        return dataModelGenerationService.generateDataModel(context);
    }
    
    public DataModelGenerationResult executeWithMemory(String userInput) {
        PromptContext context = dataModelGenerationService.buildConversationContext(userInput, true);
        return dataModelGenerationService.generateDataModelWithMemory(context);
    }
    
    public DataModelGenerationResult continueConversation(String conversationId, String userInput) {
        return dataModelGenerationService.continueConversation(conversationId, userInput);
    }
    
    public DataModelGenerationResult refineDataModel(String sessionId, String refinementInstructions) {
        return dataModelGenerationService.refineDataModel(sessionId, refinementInstructions);
    }
    
    public DataModelGenerationResult refineDataModelWithMemory(String conversationId, String refinementInstructions) {
        return dataModelGenerationService.refineDataModelWithMemory(conversationId, refinementInstructions);
    }
}