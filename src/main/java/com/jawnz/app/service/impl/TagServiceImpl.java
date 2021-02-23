package com.jawnz.app.service.impl;

import com.jawnz.app.service.TagService;
import com.jawnz.app.domain.Tag;
import com.jawnz.app.repository.TagRepository;
import com.jawnz.app.repository.search.TagSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Tag}.
 */
@Service
public class TagServiceImpl implements TagService {

    private final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    private final TagRepository tagRepository;

    private final TagSearchRepository tagSearchRepository;

    public TagServiceImpl(TagRepository tagRepository, TagSearchRepository tagSearchRepository) {
        this.tagRepository = tagRepository;
        this.tagSearchRepository = tagSearchRepository;
    }

    @Override
    public Tag save(Tag tag) {
        log.debug("Request to save Tag : {}", tag);
        Tag result = tagRepository.save(tag);
        tagSearchRepository.save(result);
        return result;
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        log.debug("Request to get all Tags");
        return tagRepository.findAll(pageable);
    }


    @Override
    public Optional<Tag> findOne(String id) {
        log.debug("Request to get Tag : {}", id);
        return tagRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Tag : {}", id);
        tagRepository.deleteById(id);
        tagSearchRepository.deleteById(id);
    }

    @Override
    public Page<Tag> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Tags for query {}", query);
        return tagSearchRepository.search(queryStringQuery(query), pageable);    }
}
