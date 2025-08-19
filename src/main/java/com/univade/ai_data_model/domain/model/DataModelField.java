package com.univade.ai_data_model.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "data_model_fields")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataModelField {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String dataType;
    
    @Column
    private Integer length;
    
    @Column
    private Integer precision;
    
    @Column
    private Integer scale;
    
    @Column(nullable = false)
    private Boolean nullable;
    
    @Column
    private String defaultValue;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column
    private String constraints;
    
    @Column
    private Boolean isPrimaryKey;
    
    @Column
    private Boolean isUnique;
    
    @Column
    private Boolean isIndexed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    private DataModelEntity entity;
}
