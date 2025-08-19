package com.univade.ai_data_model.domain.service;

import com.univade.ai_data_model.domain.model.PromptContext;
import org.springframework.stereotype.Component;

@Component
public class PromptRefiner {
    
    public String buildUserPrompt(PromptContext context) {
        StringBuilder promptBuilder = new StringBuilder();
        
        // Add the user input
        promptBuilder.append("Generate data model based on the following requirements: \n");
        promptBuilder.append(context.getUserInput());
        
        return promptBuilder.toString();
    }
    
    public String refinePrompt(PromptContext context, String refinementInstructions) {
        StringBuilder promptBuilder = new StringBuilder();
        
        promptBuilder.append("Based on the previously generated data model, please apply the following refinements: \n");
        promptBuilder.append(refinementInstructions);
        
        return promptBuilder.toString();
    }
}