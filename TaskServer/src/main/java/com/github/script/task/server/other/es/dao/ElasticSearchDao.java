package com.github.script.task.server.other.es.dao;

import com.github.script.task.server.other.es.domain.SuperEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * ES的dao
 *
 * @param <T>
 */
public interface ElasticSearchDao<T extends SuperEntity> extends ElasticsearchRepository<T, String> {

}
