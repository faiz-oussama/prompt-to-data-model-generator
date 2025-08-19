package com.univade.ai_data_model.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatMemoryConfig {

    @Bean
    @Primary
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(50)
                .build();
    }
}