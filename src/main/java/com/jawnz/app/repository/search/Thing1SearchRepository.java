package com.jawnz.app.repository.search;

import com.jawnz.app.domain.Thing1;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Thing1} entity.
 */
public interface Thing1SearchRepository extends ElasticsearchRepository<Thing1, String> {
}
