package com.univade.ai_data_model.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "data_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String prompt;
    
    @Column(columnDefinition = "TEXT")
    private String generatedCode;
    
    @Enumerated(EnumType.STRING)
    private DataModelType type;
    
    @Enumerated(EnumType.STRING)
    private DataModelStatus status;
    
    @OneToMany(mappedBy = "dataModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DataModelEntity> entities;
    
    @OneToMany(mappedBy = "dataModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DataModelRelationship> relationships;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime updatedAt;
    
    @Column(nullable = false)
    private String sessionId;
    
    @Column(columnDefinition = "TEXT")
    private String contextSummary;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
