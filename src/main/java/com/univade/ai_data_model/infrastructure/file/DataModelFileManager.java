package com.univade.ai_data_model.infrastructure.file;

import com.univade.ai_data_model.domain.model.DataModelGenerationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DataModelFileManager {
    
    private static final Logger logger = LoggerFactory.getLogger(DataModelFileManager.class);
    
    @Value("${data.model.generation.output-directory:./generated-models}")
    private String outputDirectory;
    
    public void saveGeneratedDataModel(DataModelGenerationResult result) {
        try {
            if (result.getDataModelJson() == null || result.getDataModelJson().isEmpty()) {
                logger.warn("No data model content to save for session: {}", result.getSessionId());
                return;
            }
            
            String sessionId = result.getSessionId();
            String outputDir = outputDirectory + "/" + sessionId;
            
            Path directoryPath = Paths.get(outputDir);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String modelType = result.getMetadata() != null ? result.getMetadata().getModelType() : "model";
            
            Path metadataPath = Paths.get(outputDir, "metadata.json");
            Path modelPath = Paths.get(outputDir, modelType + "_" + timestamp + ".json");
            
            // Save metadata
            if (result.getMetadata() != null) {
                Files.writeString(metadataPath, formatJson(result.getMetadata().toString()));
            }
            
            // Save data model
            Files.writeString(modelPath, formatJson(result.getDataModelJson()));
            
            logger.info("Saved generated data model to {}", modelPath);
            
        } catch (IOException e) {
            logger.error("Failed to save generated data model", e);
        }
    }
    
    private String formatJson(String json) {
        // format basic pour le moment !
        return json;
    }
}