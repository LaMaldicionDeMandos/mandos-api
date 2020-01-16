package org.pasut.smarthome.mandosapi.config;

import org.pasut.smarthome.mandosapi.model.ShoppingListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoMappingContext mongoMappingContext;

    @EventListener(ApplicationReadyEvent.class)
    void initIndicesAfterStartup() {
	    IndexOperations indexOps = mongoTemplate.indexOps(ShoppingListItem.class);
	    IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
	    resolver.resolveIndexFor(ShoppingListItem.class).forEach(indexOps::ensureIndex);
    }
}
