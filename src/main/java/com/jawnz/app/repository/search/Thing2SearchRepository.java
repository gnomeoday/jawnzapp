package com.jawnz.app.repository.search;

import com.jawnz.app.domain.Thing2;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Thing2} entity.
 */
public interface Thing2SearchRepository extends ElasticsearchRepository<Thing2, String> {
}
