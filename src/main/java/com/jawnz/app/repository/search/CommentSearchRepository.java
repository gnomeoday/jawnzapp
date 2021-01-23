package com.jawnz.app.repository.search;

import com.jawnz.app.domain.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Comment} entity.
 */
public interface CommentSearchRepository extends ElasticsearchRepository<Comment, String> {
}
