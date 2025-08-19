package com.univade.ai_data_model.domain.service;

import com.univade.ai_data_model.domain.model.DataModelGenerationResult;
import org.springframework.stereotype.Component;

@Component
public class DataModelResultProcessor {

    public boolean isValidDataModelResult(DataModelGenerationResult result) {
        return result != null 
            && "SUCCESS".equals(result.getStatus())
            && result.getDataModelJson() != null 
            && !result.getDataModelJson().isEmpty();
    }
    
    public boolean isJsonFormat(String content) {
        return content != null && content.trim().startsWith("{") && content.trim().endsWith("}");
    }
}