package com.univade.ai_data_model.infrastructure.persistence;

import com.univade.ai_data_model.domain.model.DataModelGenerationResult;
import com.univade.ai_data_model.domain.repository.DataModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryDataModelRepository implements DataModelRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryDataModelRepository.class);

    private final ConcurrentMap<String, DataModelGenerationResult> idToResult = new ConcurrentHashMap<>();

    @Override
    public void save(DataModelGenerationResult result) {
        if (result.getId() == null) {
            // DataModelGenerationResult always has an id; but guard just in case
            result.setId(java.util.UUID.randomUUID().toString());
        }
        idToResult.put(result.getId(), result);
        logger.debug("Saved data model result: {}", result.getId());
    }

    @Override
    public Optional<DataModelGenerationResult> findById(String id) {
        return Optional.ofNullable(idToResult.get(id));
    }

    @Override
    public List<DataModelGenerationResult> findBySessionId(String sessionId) {
        List<DataModelGenerationResult> results = new ArrayList<>();
        for (DataModelGenerationResult r : idToResult.values()) {
            if (sessionId != null && sessionId.equals(r.getSessionId())) {
                results.add(r);
            }
        }
        return results;
    }

    @Override
    public List<DataModelGenerationResult> findByConversationId(String conversationId) {
        List<DataModelGenerationResult> results = new ArrayList<>();
        for (DataModelGenerationResult r : idToResult.values()) {
            if (conversationId != null && conversationId.equals(r.getConversationId())) {
                results.add(r);
            }
        }
        return results;
    }

    @Override
    public void deleteById(String id) {
        idToResult.remove(id);
        logger.debug("Deleted data model result: {}", id);
    }
}



