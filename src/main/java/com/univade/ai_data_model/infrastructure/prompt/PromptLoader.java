package com.univade.ai_data_model.infrastructure.prompt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PromptLoader {
    
    private static final Logger logger = LoggerFactory.getLogger(PromptLoader.class);
    
    @Value("${data.model.system-prompt-path:prompts/data_model_system_prompt.md}")
    private String systemPromptPath;
    
    public String loadSystemPrompt() {
        try {
            // First try to load from classpath
            try {
                ClassPathResource resource = new ClassPathResource(systemPromptPath);
                return new String(resource.getInputStream().readAllBytes());
            } catch (IOException e) {
                logger.debug("Could not load system prompt from classpath, trying file system");
            }
            
            // Try to load from file system
            Path path = Paths.get(systemPromptPath);
            if (Files.exists(path)) {
                return Files.readString(path);
            }
            
            logger.error("System prompt file not found at path: {}", systemPromptPath);
            return "You are an AI data model generator that creates data models based on user requirements.";
            
        } catch (IOException e) {
            logger.error("Failed to load system prompt", e);
            return "You are an AI data model generator that creates data models based on user requirements.";
        }
    }
}