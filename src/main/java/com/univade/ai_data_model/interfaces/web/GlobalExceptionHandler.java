package com.univade.ai_data_model.interfaces.web;

import com.univade.ai_data_model.domain.exception.ConversationNotFoundException;
import com.univade.ai_data_model.domain.exception.DataModelGenerationException;
import com.univade.ai_data_model.interfaces.dto.DataModelResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(ConversationNotFoundException.class)
    public ResponseEntity<DataModelResponseDTO> handleConversationNotFoundException(ConversationNotFoundException ex) {
        logger.error("Conversation not found", ex);
        
        DataModelResponseDTO response = new DataModelResponseDTO();
        response.setStatus("ERROR");
        response.setErrorMessage(ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(DataModelGenerationException.class)
    public ResponseEntity<DataModelResponseDTO> handleDataModelGenerationException(DataModelGenerationException ex) {
        logger.error("Error generating data model", ex);
        
        DataModelResponseDTO response = new DataModelResponseDTO();
        response.setStatus("ERROR");
        response.setErrorMessage(ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataModelResponseDTO> handleGenericException(Exception ex) {
        logger.error("Unexpected error", ex);
        
        DataModelResponseDTO response = new DataModelResponseDTO();
        response.setStatus("ERROR");
        response.setErrorMessage("Internal server error: " + ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}