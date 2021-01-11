package com.jawnz.app.repository;

import com.jawnz.app.domain.Thing1;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Thing1 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Thing1Repository extends MongoRepository<Thing1, String> {
}
