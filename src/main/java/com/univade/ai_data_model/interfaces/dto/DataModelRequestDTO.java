package com.univade.ai_data_model.interfaces.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataModelRequestDTO {
    private String userInput;
    private String sessionId;
    private String conversationId;

    public boolean isContinuingConversation() {
        return conversationId != null && !conversationId.trim().isEmpty();
    }
}