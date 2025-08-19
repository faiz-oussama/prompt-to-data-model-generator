package com.univade.ai_data_model.infrastructure.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.univade.ai_data_model.domain.model.DataModelGenerationResult;
import java.util.UUID;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class OpenAiClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiClient.class);

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public OpenAiClient(ChatClient chatClient,
                       ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.objectMapper = objectMapper;
    }

    public DataModelGenerationResult generateDataModel(String systemPrompt, String userPrompt) {
        try {
            String response = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();

            if (response == null || response.trim().isEmpty()) {
                return createErrorResult("Empty response from AI");
            }

            return parseAiResponse(response);

        } catch (Exception e) {
            logger.error("Error generating data model with AI", e);
            return createErrorResult("Failed to generate data model: " + e.getMessage());
        }
    }

    public DataModelGenerationResult refineDataModel(String systemPrompt, String refinementPrompt) {
        try {
            String response = chatClient.prompt()
                .system(systemPrompt)
                .user(refinementPrompt)
                .call()
                .content();

            if (response == null || response.trim().isEmpty()) {
                return createErrorResult("Empty response from AI");
            }

            return parseAiResponse(response);

        } catch (Exception e) {
            logger.error("Error refining data model with AI", e);
            return createErrorResult("Failed to refine data model: " + e.getMessage());
        }
    }

    public DataModelGenerationResult generateDataModelWithMemory(String systemPrompt, String userPrompt, String conversationId) {
        try {
            String response = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .advisors(advisorSpec -> advisorSpec.param("conversationId", conversationId))
                .call()
                .content();

            if (response == null || response.trim().isEmpty()) {
                return createErrorResult("Empty response from AI");
            }

            return parseAiResponse(response);

        } catch (Exception e) {
            logger.error("Error generating data model with memory for conversation: {}", conversationId, e);
            return createErrorResult("Failed to generate data model: " + e.getMessage());
        }
    }

    public DataModelGenerationResult refineDataModelWithMemory(String systemPrompt, String refinementPrompt, String conversationId) {
        try {
            String response = chatClient.prompt()
                .system(systemPrompt)
                .user(refinementPrompt)
                .advisors(advisorSpec -> advisorSpec.param("conversationId", conversationId))
                .call()
                .content();

            if (response == null || response.trim().isEmpty()) {
                return createErrorResult("Empty response from AI");
            }

            return parseAiResponse(response);

        } catch (Exception e) {
            logger.error("Error refining data model with memory for conversation: {}", conversationId, e);
            return createErrorResult("Failed to refine data model: " + e.getMessage());
        }
    }

    private DataModelGenerationResult parseAiResponse(String response) {
        try {
            String jsonResponse = extractJsonFromResponse(response);

            if (jsonResponse == null) {
                // If we couldn't find a JSON object, try to parse the entire response as JSON
                if (response.trim().startsWith("{") && response.trim().endsWith("}")) {
                    jsonResponse = response;
                } else {
                    // If it's not a JSON, create a result with raw text
                    DataModelGenerationResult result = new DataModelGenerationResult(UUID.randomUUID().toString());
                    result.setStatus("SUCCESS");
                    result.setDataModelJson(response);
                    result.setGeneratedAt(LocalDateTime.now());
                    return result;
                }
            }

            return objectMapper.readValue(jsonResponse, DataModelGenerationResult.class);

        } catch (JsonProcessingException e) {
            logger.error("Error parsing AI response JSON", e);
            
            // If JSON parsing fails, still try to return the response as raw text
            DataModelGenerationResult result = new DataModelGenerationResult(UUID.randomUUID().toString());
            result.setStatus("SUCCESS");
            result.setDataModelJson(response);
            result.setGeneratedAt(LocalDateTime.now());
            return result;
        }
    }

    private String extractJsonFromResponse(String response) {
        int jsonStart = response.indexOf("{");
        if (jsonStart == -1) {
            return null;
        }

        int braceCount = 0;
        int jsonEnd = -1;

        for (int i = jsonStart; i < response.length(); i++) {
            char c = response.charAt(i);
            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0) {
                    jsonEnd = i;
                    break;
                }
            }
        }

        if (jsonEnd == -1) {
            return null;
        }

        return response.substring(jsonStart, jsonEnd + 1);
    }

    private DataModelGenerationResult createErrorResult(String errorMessage) {
        DataModelGenerationResult result = new DataModelGenerationResult(UUID.randomUUID().toString());
        result.setStatus("ERROR");
        result.setErrorMessage(errorMessage);
        result.setGeneratedAt(LocalDateTime.now());
        return result;
    }
}