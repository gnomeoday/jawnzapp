package com.jawnz.app.service;

import com.jawnz.app.domain.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Tag}.
 */
public interface TagService {

    /**
     * Save a tag.
     *
     * @param tag the entity to save.
     * @return the persisted entity.
     */
    Tag save(Tag tag);

    /**
     * Get all the tags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tag> findAll(Pageable pageable);


    /**
     * Get the "id" tag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tag> findOne(String id);

    /**
     * Delete the "id" tag.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the tag corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tag> search(String query, Pageable pageable);
}
