package com.univade.ai_data_model.domain.exception;

public class DataModelGenerationException extends RuntimeException {
    public DataModelGenerationException(String message) {
        super(message);
    }

    public DataModelGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}