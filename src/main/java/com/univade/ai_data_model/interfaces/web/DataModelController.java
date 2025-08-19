package com.univade.ai_data_model.interfaces.web;

import com.univade.ai_data_model.application.service.DataModelGenerationService;
import com.univade.ai_data_model.domain.model.PromptContext;
import com.univade.ai_data_model.domain.model.DataModelGenerationResult;
import com.univade.ai_data_model.interfaces.dto.DataModelRequestDTO;
import com.univade.ai_data_model.interfaces.dto.DataModelResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-model")
public class DataModelController {
    private final DataModelGenerationService dataModelGenerationService;

    public DataModelController(DataModelGenerationService dataModelGenerationService) {
        this.dataModelGenerationService = dataModelGenerationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<DataModelResponseDTO> generateDataModel(@RequestBody DataModelRequestDTO request) {
        try {
            PromptContext context = buildContextAwarePromptContext(request);
            DataModelGenerationResult result = dataModelGenerationService.generateDataModelWithMemory(context);
            DataModelResponseDTO response = convertToContextAwareResponseDTO(result);

            return "ERROR".equals(result.getStatus())
                ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
                : ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse(null, "Internal server error"));
        }
    }

    private PromptContext buildContextAwarePromptContext(DataModelRequestDTO request) {
        if (request.isContinuingConversation()) {
            PromptContext context = new PromptContext(
                    request.getSessionId(),
                    request.getConversationId(),
                    request.getUserInput());
            context.setUseConversationMemory(true);
            return context;
        } else {
            PromptContext context = dataModelGenerationService.buildConversationContext(
                    request.getUserInput(),
                    true);

            if (request.getSessionId() != null) {
                context.setSessionId(request.getSessionId());
            }

            return context;
        }
    }

    private DataModelResponseDTO convertToContextAwareResponseDTO(DataModelGenerationResult result) {
        DataModelResponseDTO response = convertToResponseDTO(result);
        response.setConversationId(result.getConversationId());
        response.setNewConversation(result.isNewConversation());
        return response;
    }

    private DataModelResponseDTO convertToResponseDTO(DataModelGenerationResult result) {
        DataModelResponseDTO response = new DataModelResponseDTO();
        response.setSessionId(result.getSessionId());
        response.setStatus(result.getStatus());
        response.setErrorMessage(result.getErrorMessage());
        response.setGeneratedAt(result.getGeneratedAt());

        if (result.getDataModelJson() != null) {
            response.setGeneratedDataModel(result.getDataModelJson());
        }

        if (result.getMetadata() != null) {
            response.setModelType(result.getMetadata().getModelType());
            response.setTargetFormat(result.getMetadata().getTargetFormat());
        }

        return response;
    }

    private DataModelResponseDTO createErrorResponse(String sessionId, String errorMessage) {
        DataModelResponseDTO response = new DataModelResponseDTO();
        response.setSessionId(sessionId);
        response.setStatus("ERROR");
        response.setErrorMessage(errorMessage);
        return response;
    }
}