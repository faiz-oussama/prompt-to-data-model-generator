package com.univade.ai_data_model.interfaces.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataModelResponseDTO {
    private String sessionId;
    private String conversationId;
    private String generatedDataModel;
    private String modelType;
    private String targetFormat;
    private String status;
    private String errorMessage;
    private LocalDateTime generatedAt;
    private boolean isNewConversation;
}