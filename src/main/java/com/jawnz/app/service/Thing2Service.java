package com.jawnz.app.service;

import com.jawnz.app.domain.Thing2;
import com.jawnz.app.repository.Thing2Repository;
import com.jawnz.app.repository.search.Thing2SearchRepository;
import com.jawnz.app.service.dto.Thing2DTO;
import com.jawnz.app.service.mapper.Thing2Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Thing2}.
 */
@Service
public class Thing2Service {

    private final Logger log = LoggerFactory.getLogger(Thing2Service.class);

    private final Thing2Repository thing2Repository;

    private final Thing2Mapper thing2Mapper;

    private final Thing2SearchRepository thing2SearchRepository;

    public Thing2Service(Thing2Repository thing2Repository, Thing2Mapper thing2Mapper, Thing2SearchRepository thing2SearchRepository) {
        this.thing2Repository = thing2Repository;
        this.thing2Mapper = thing2Mapper;
        this.thing2SearchRepository = thing2SearchRepository;
    }

    /**
     * Save a thing2.
     *
     * @param thing2DTO the entity to save.
     * @return the persisted entity.
     */
    public Thing2DTO save(Thing2DTO thing2DTO) {
        log.debug("Request to save Thing2 : {}", thing2DTO);
        Thing2 thing2 = thing2Mapper.toEntity(thing2DTO);
        thing2 = thing2Repository.save(thing2);
        Thing2DTO result = thing2Mapper.toDto(thing2);
        thing2SearchRepository.save(thing2);
        return result;
    }

    /**
     * Get all the thing2s.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Thing2DTO> findAll(Pageable pageable) {
        log.debug("Request to get all Thing2s");
        return thing2Repository.findAll(pageable)
            .map(thing2Mapper::toDto);
    }



    /**
     *  Get all the thing2s where Child is {@code null}.
     *  @return the list of entities.
     */
    public List<Thing2DTO> findAllWhereChildIsNull() {
        log.debug("Request to get all thing2s where Child is null");
        return StreamSupport
            .stream(thing2Repository.findAll().spliterator(), false)
            .filter(thing2 -> thing2.getChild() == null)
            .map(thing2Mapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one thing2 by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Thing2DTO> findOne(String id) {
        log.debug("Request to get Thing2 : {}", id);
        return thing2Repository.findById(id)
            .map(thing2Mapper::toDto);
    }

    /**
     * Delete the thing2 by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Thing2 : {}", id);
        thing2Repository.deleteById(id);
        thing2SearchRepository.deleteById(id);
    }

    /**
     * Search for the thing2 corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Thing2DTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Thing2s for query {}", query);
        return thing2SearchRepository.search(queryStringQuery(query), pageable)
            .map(thing2Mapper::toDto);
    }
}
