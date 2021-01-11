package com.jawnz.app.repository;

import com.jawnz.app.domain.Thing2;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Thing2 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Thing2Repository extends MongoRepository<Thing2, String> {
}
