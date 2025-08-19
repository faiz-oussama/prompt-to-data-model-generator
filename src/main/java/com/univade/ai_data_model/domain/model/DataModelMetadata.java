package com.univade.ai_data_model.domain.model;

import java.util.List;

public class DataModelMetadata {
    private String modelType;
    private String targetFormat;
    private int entityCount;
    private int relationshipCount;
    private List<String> entities;
    private List<String> relationships;
    private String generationFramework;
    
    public DataModelMetadata() {
    }
    
    public DataModelMetadata(String modelType, String targetFormat, int entityCount, int relationshipCount, 
                            List<String> entities, List<String> relationships, String generationFramework) {
        this.modelType = modelType;
        this.targetFormat = targetFormat;
        this.entityCount = entityCount;
        this.relationshipCount = relationshipCount;
        this.entities = entities;
        this.relationships = relationships;
        this.generationFramework = generationFramework;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getTargetFormat() {
        return targetFormat;
    }

    public void setTargetFormat(String targetFormat) {
        this.targetFormat = targetFormat;
    }

    public int getEntityCount() {
        return entityCount;
    }

    public void setEntityCount(int entityCount) {
        this.entityCount = entityCount;
    }

    public int getRelationshipCount() {
        return relationshipCount;
    }

    public void setRelationshipCount(int relationshipCount) {
        this.relationshipCount = relationshipCount;
    }

    public List<String> getEntities() {
        return entities;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }

    public List<String> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<String> relationships) {
        this.relationships = relationships;
    }

    public String getGenerationFramework() {
        return generationFramework;
    }

    public void setGenerationFramework(String generationFramework) {
        this.generationFramework = generationFramework;
    }

    @Override
    public String toString() {
        return "DataModelMetadata{" +
                "modelType='" + modelType + '\'' +
                ", targetFormat='" + targetFormat + '\'' +
                ", entityCount=" + entityCount +
                ", relationshipCount=" + relationshipCount +
                ", entities=" + entities +
                ", relationships=" + relationships +
                ", generationFramework='" + generationFramework + '\'' +
                '}';
    }
}