package com.univade.ai_data_model.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "data_model_entities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataModelEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String tableName;
    
    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DataModelField> fields;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_model_id", nullable = false)
    private DataModel dataModel;
    
    @Column
    private String primaryKey;
    
    @Column
    private String indexes;
    
    @Column
    private String constraints;
}
