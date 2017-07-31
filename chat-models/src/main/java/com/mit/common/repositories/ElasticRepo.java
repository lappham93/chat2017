package com.mit.common.repositories;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * Created by Hung Le on 6/14/17.
 */

@Repository
public class ElasticRepo<T> {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private Class<T> entityClass;

    private Class<T> getGenericClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();

        @SuppressWarnings("unchecked")
        Class<T> ret = (Class<T>) parameterizedType.getActualTypeArguments()[0];

        return ret;
    }

    @PostConstruct
    public void init() {
        entityClass = getGenericClass();
        if (!elasticsearchTemplate.indexExists(entityClass)) {
            elasticsearchTemplate.createIndex(entityClass);
            elasticsearchTemplate.putMapping(entityClass);
        }
    }

    public void save(Object entity) {
        ElasticsearchPersistentEntity persistentEntity = elasticsearchTemplate.getPersistentEntityFor(entity.getClass());
        Object id = persistentEntity.getPropertyAccessor(entity).getProperty(persistentEntity.getIdProperty());
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(String.valueOf(id))
                .withIndexName(persistentEntity.getIndexName()).withObject(entity)
                .withType(persistentEntity.getIndexType()).build();

        elasticsearchTemplate.index(indexQuery);
    }
    
    public void saveBatch(List<Object> entities) {
    	if (CollectionUtils.isEmpty(entities)) {
    		return;
    	}
        ElasticsearchPersistentEntity persistentEntity = elasticsearchTemplate.getPersistentEntityFor(entities.get(0).getClass());
        List<IndexQuery> queries = new LinkedList<>();
        for (Object entity: entities) {
	        Object id = persistentEntity.getPropertyAccessor(entity).getProperty(persistentEntity.getIdProperty());
	        IndexQuery indexQuery = new IndexQueryBuilder()
	                .withId(String.valueOf(id))
	                .withIndexName(persistentEntity.getIndexName()).withObject(entity)
	                .withType(persistentEntity.getIndexType()).build();
	        queries.add(indexQuery);
        }

        elasticsearchTemplate.bulkIndex(queries);
    }
    
    public void delete(String id) {
    	elasticsearchTemplate.delete(entityClass, id);
    }
    
}
