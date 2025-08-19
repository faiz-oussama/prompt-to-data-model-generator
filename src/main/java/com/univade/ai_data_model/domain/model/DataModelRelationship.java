package com.univade.ai_data_model.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "data_model_relationships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataModelRelationship {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private RelationshipType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_entity_id", nullable = false)
    private DataModelEntity sourceEntity;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_entity_id", nullable = false)
    private DataModelEntity targetEntity;
    
    @Column(nullable = false)
    private String sourceField;
    
    @Column(nullable = false)
    private String targetField;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column
    private Boolean cascadeDelete;
    
    @Column
    private Boolean cascadeUpdate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_model_id", nullable = false)
    private DataModel dataModel;
}
