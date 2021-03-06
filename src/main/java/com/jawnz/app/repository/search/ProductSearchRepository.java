package com.jawnz.app.repository.search;

import com.jawnz.app.domain.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Product} entity.
 */
public interface ProductSearchRepository extends ElasticsearchRepository<Product, String> {
}
