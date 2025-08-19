package com.univade.ai_data_model.application.service;

import com.univade.ai_data_model.domain.model.*;
import com.univade.ai_data_model.domain.repository.DataModelRepository;
import com.univade.ai_data_model.domain.repository.MemoryRepository;
import com.univade.ai_data_model.domain.service.DataModelResultProcessor;
import com.univade.ai_data_model.domain.service.PromptRefiner;
import com.univade.ai_data_model.infrastructure.ai.OpenAiClient;
import com.univade.ai_data_model.infrastructure.file.DataModelFileManager;
import com.univade.ai_data_model.infrastructure.prompt.PromptLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DataModelGenerationServiceImpl implements DataModelGenerationService {

    private static final Logger logger = LoggerFactory.getLogger(DataModelGenerationServiceImpl.class);

    private final OpenAiClient openAiClient;
    private final PromptLoader promptLoader;
    private final PromptRefiner promptRefiner;
    private final MemoryRepository memoryRepository;
    private final DataModelFileManager dataModelFileManager;
    private final DataModelResultProcessor dataModelResultProcessor;
    private final ConversationService conversationService;
    private final DataModelRepository dataModelRepository;

    public DataModelGenerationServiceImpl(OpenAiClient openAiClient,
                                     PromptLoader promptLoader,
                                     PromptRefiner promptRefiner,
                                     MemoryRepository memoryRepository,
                                     DataModelFileManager dataModelFileManager,
                                     DataModelResultProcessor dataModelResultProcessor,
                                     ConversationService conversationService,
                                     DataModelRepository dataModelRepository) {
        this.openAiClient = openAiClient;
        this.promptLoader = promptLoader;
        this.promptRefiner = promptRefiner;
        this.memoryRepository = memoryRepository;
        this.dataModelFileManager = dataModelFileManager;
        this.dataModelResultProcessor = dataModelResultProcessor;
        this.conversationService = conversationService;
        this.dataModelRepository = dataModelRepository;
    }

    @Override
    public DataModelGenerationResult generateDataModel(PromptContext context) {
        try {
            if (context.getSessionId() == null || context.getSessionId().trim().isEmpty()) {
                context.setSessionId(UUID.randomUUID().toString());
            }

            memoryRepository.save(context.getSessionId(), context);

            String systemPrompt = promptLoader.loadSystemPrompt();
            String userPrompt = promptRefiner.buildUserPrompt(context);

            DataModelGenerationResult result = openAiClient.generateDataModel(systemPrompt, userPrompt);
            result.setSessionId(context.getSessionId());

            if ("SUCCESS".equals(result.getStatus()) && dataModelResultProcessor.isValidDataModelResult(result)) {
                try {
                    dataModelFileManager.saveGeneratedDataModel(result);
                    dataModelRepository.save(result);
                } catch (Exception e) {
                    logger.warn("Failed to save data model for session: {}", context.getSessionId(), e);
                }
            }

            return result;

        } catch (Exception e) {
            logger.error("Error generating data model", e);
            DataModelGenerationResult errorResult = new DataModelGenerationResult(context.getSessionId());
            errorResult.setStatus("ERROR");
            errorResult.setErrorMessage("Failed to generate data model: " + e.getMessage());
            return errorResult;
        }
    }

    @Override
    public DataModelGenerationResult generateDataModelWithMemory(PromptContext context) {
        try {
            if (context.getSessionId() == null || context.getSessionId().trim().isEmpty()) {
                context.setSessionId(UUID.randomUUID().toString());
            }

            boolean isNewConversation = false;
            if (context.getConversationId() == null && context.isUseConversationMemory()) {
                String conversationId = conversationService.startConversation("default-user", context.getSessionId());
                context.setConversationId(conversationId);
                isNewConversation = true;
            }

            memoryRepository.save(context.getSessionId(), context);

            String systemPrompt = promptLoader.loadSystemPrompt();
            String userPrompt = promptRefiner.buildUserPrompt(context);

            DataModelGenerationResult result;
            if (context.shouldUseConversationMemory()) {
                result = openAiClient.generateDataModelWithMemory(systemPrompt, userPrompt, context.getConversationId());

                conversationService.incrementMessageCount(context.getConversationId());

                context.addToHistory(ConversationMessage.userMessage(context.getConversationId(), userPrompt));
                if (result.getDataModelJson() != null) {
                    context.addToHistory(ConversationMessage.assistantMessage(context.getConversationId(),
                            "Generated data model successfully"));
                }
            } else {
                result = openAiClient.generateDataModel(systemPrompt, userPrompt);
            }

            result.setSessionId(context.getSessionId());
            if (context.getConversationId() != null) {
                result.setConversationId(context.getConversationId());
            }
            result.setNewConversation(isNewConversation);

            if ("SUCCESS".equals(result.getStatus()) && dataModelResultProcessor.isValidDataModelResult(result)) {
                try {
                    dataModelFileManager.saveGeneratedDataModel(result);
                    dataModelRepository.save(result);
                } catch (Exception e) {
                    logger.warn("Failed to save data model for session: {}", context.getSessionId(), e);
                }
            }

            return result;

        } catch (Exception e) {
            logger.error("Error generating data model with memory", e);
            DataModelGenerationResult errorResult = new DataModelGenerationResult(context.getSessionId());
            errorResult.setStatus("ERROR");
            errorResult.setErrorMessage("Failed to generate data model: " + e.getMessage());
            return errorResult;
        }
    }

    @Override
    public DataModelGenerationResult refineDataModel(String sessionId, String refinementInstructions) {
        try {
            Optional<PromptContext> contextOpt = memoryRepository.findBySessionId(sessionId);
            if (contextOpt.isEmpty()) {
                DataModelGenerationResult errorResult = new DataModelGenerationResult(sessionId);
                errorResult.setStatus("ERROR");
                errorResult.setErrorMessage("Session not found: " + sessionId);
                return errorResult;
            }

            PromptContext context = contextOpt.get();
            String systemPrompt = promptLoader.loadSystemPrompt();
            String refinementPrompt = promptRefiner.refinePrompt(context, refinementInstructions);

            DataModelGenerationResult result = openAiClient.refineDataModel(systemPrompt, refinementPrompt);
            result.setSessionId(sessionId);

            if ("SUCCESS".equals(result.getStatus()) && dataModelResultProcessor.isValidDataModelResult(result)) {
                try {
                    dataModelFileManager.saveGeneratedDataModel(result);
                    dataModelRepository.save(result);
                } catch (Exception e) {
                    logger.warn("Failed to save refined data model for session: {}", sessionId, e);
                }
            }

            return result;

        } catch (Exception e) {
            logger.error("Error refining data model for session: {}", sessionId, e);
            DataModelGenerationResult errorResult = new DataModelGenerationResult(sessionId);
            errorResult.setStatus("ERROR");
            errorResult.setErrorMessage("Failed to refine data model: " + e.getMessage());
            return errorResult;
        }
    }

    @Override
    public DataModelGenerationResult refineDataModelWithMemory(String conversationId, String refinementInstructions) {
        try {
            Optional<ConversationContext> contextOpt = conversationService.getConversationContext(conversationId);
            if (contextOpt.isEmpty()) {
                DataModelGenerationResult errorResult = new DataModelGenerationResult();
                errorResult.setStatus("ERROR");
                errorResult.setErrorMessage("Conversation not found: " + conversationId);
                return errorResult;
            }

            ConversationContext conversationContext = contextOpt.get();

            Optional<PromptContext> promptContextOpt = memoryRepository.findBySessionId(conversationContext.getSessionId());
            if (promptContextOpt.isEmpty()) {
                DataModelGenerationResult errorResult = new DataModelGenerationResult();
                errorResult.setStatus("ERROR");
                errorResult.setErrorMessage("Session context not found: " + conversationContext.getSessionId());
                return errorResult;
            }

            PromptContext context = promptContextOpt.get();
            String systemPrompt = promptLoader.loadSystemPrompt();
            String refinementPrompt = promptRefiner.refinePrompt(context, refinementInstructions);

            DataModelGenerationResult result = openAiClient.refineDataModelWithMemory(systemPrompt, refinementPrompt, conversationId);
            result.setSessionId(conversationContext.getSessionId());
            result.setConversationId(conversationId);

            // Track conversation metrics
            conversationService.incrementMessageCount(conversationId);

            context.addToHistory(ConversationMessage.userMessage(conversationId, refinementInstructions));
            if (result.getDataModelJson() != null) {
                context.addToHistory(ConversationMessage.assistantMessage(conversationId,
                        "Refined data model successfully"));
            }

            if ("SUCCESS".equals(result.getStatus()) && dataModelResultProcessor.isValidDataModelResult(result)) {
                try {
                    dataModelFileManager.saveGeneratedDataModel(result);
                    dataModelRepository.save(result);
                } catch (Exception e) {
                    logger.warn("Failed to save refined data model for conversation: {}", conversationId, e);
                }
            }

            return result;

        } catch (Exception e) {
            logger.error("Error refining data model with memory for conversation: {}", conversationId, e);
            DataModelGenerationResult errorResult = new DataModelGenerationResult();
            errorResult.setStatus("ERROR");
            errorResult.setErrorMessage("Failed to refine data model: " + e.getMessage());
            return errorResult;
        }
    }

    @Override
    public DataModelGenerationResult continueConversation(String conversationId, String userInput) {
        try {
            Optional<ConversationContext> contextOpt = conversationService.getConversationContext(conversationId);
            if (contextOpt.isEmpty()) {
                DataModelGenerationResult errorResult = new DataModelGenerationResult();
                errorResult.setStatus("ERROR");
                errorResult.setErrorMessage("Conversation not found: " + conversationId);
                return errorResult;
            }

            ConversationContext conversationContext = contextOpt.get();
            PromptContext promptContext = new PromptContext(conversationContext.getSessionId(),
                    conversationId, userInput);
            promptContext.setUseConversationMemory(true);

            return generateDataModelWithMemory(promptContext);

        } catch (Exception e) {
            logger.error("Error continuing conversation: {}", conversationId, e);
            DataModelGenerationResult errorResult = new DataModelGenerationResult();
            errorResult.setStatus("ERROR");
            errorResult.setErrorMessage("Failed to continue conversation: " + e.getMessage());
            return errorResult;
        }
    }

    @Override
    public PromptContext buildContext(String userInput) {
        return new PromptContext(UUID.randomUUID().toString(), userInput);
    }

    @Override
    public PromptContext buildConversationContext(String userInput, boolean useMemory) {
        String sessionId = UUID.randomUUID().toString();
        PromptContext context = new PromptContext(sessionId, null, userInput);
        context.setUseConversationMemory(useMemory);
        return context;
    }

    @Override
    public Optional<ConversationContext> getConversationContext(String conversationId) {
        return conversationService.getConversationContext(conversationId);
    }

    @Override
    public void endConversation(String conversationId) {
        conversationService.endConversation(conversationId);
    }
}