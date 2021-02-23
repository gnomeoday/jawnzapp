package com.jawnz.app.repository;

import com.jawnz.app.domain.Comment;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Comment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
}
