package com.jawnz.app.service;

import com.jawnz.app.domain.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Comment}.
 */
public interface CommentService {

    /**
     * Save a comment.
     *
     * @param comment the entity to save.
     * @return the persisted entity.
     */
    Comment save(Comment comment);

    /**
     * Get all the comments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Comment> findAll(Pageable pageable);


    /**
     * Get the "id" comment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Comment> findOne(String id);

    /**
     * Delete the "id" comment.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the comment corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Comment> search(String query, Pageable pageable);
}
