package com.univade.ai_data_model.domain.exception;

public class ConversationNotFoundException extends RuntimeException {
    public ConversationNotFoundException(String message) {
        super(message);
    }

    public ConversationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}