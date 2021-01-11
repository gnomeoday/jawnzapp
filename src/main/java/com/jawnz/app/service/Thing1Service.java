package com.jawnz.app.service;

import com.jawnz.app.domain.Thing1;
import com.jawnz.app.repository.Thing1Repository;
import com.jawnz.app.repository.search.Thing1SearchRepository;
import com.jawnz.app.service.dto.Thing1DTO;
import com.jawnz.app.service.mapper.Thing1Mapper;
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
 * Service Implementation for managing {@link Thing1}.
 */
@Service
public class Thing1Service {

    private final Logger log = LoggerFactory.getLogger(Thing1Service.class);

    private final Thing1Repository thing1Repository;

    private final Thing1Mapper thing1Mapper;

    private final Thing1SearchRepository thing1SearchRepository;

    public Thing1Service(Thing1Repository thing1Repository, Thing1Mapper thing1Mapper, Thing1SearchRepository thing1SearchRepository) {
        this.thing1Repository = thing1Repository;
        this.thing1Mapper = thing1Mapper;
        this.thing1SearchRepository = thing1SearchRepository;
    }

    /**
     * Save a thing1.
     *
     * @param thing1DTO the entity to save.
     * @return the persisted entity.
     */
    public Thing1DTO save(Thing1DTO thing1DTO) {
        log.debug("Request to save Thing1 : {}", thing1DTO);
        Thing1 thing1 = thing1Mapper.toEntity(thing1DTO);
        thing1 = thing1Repository.save(thing1);
        Thing1DTO result = thing1Mapper.toDto(thing1);
        thing1SearchRepository.save(thing1);
        return result;
    }

    /**
     * Get all the thing1s.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Thing1DTO> findAll(Pageable pageable) {
        log.debug("Request to get all Thing1s");
        return thing1Repository.findAll(pageable)
            .map(thing1Mapper::toDto);
    }



    /**
     *  Get all the thing1s where Parent is {@code null}.
     *  @return the list of entities.
     */
    public List<Thing1DTO> findAllWhereParentIsNull() {
        log.debug("Request to get all thing1s where Parent is null");
        return StreamSupport
            .stream(thing1Repository.findAll().spliterator(), false)
            .filter(thing1 -> thing1.getParent() == null)
            .map(thing1Mapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one thing1 by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<Thing1DTO> findOne(String id) {
        log.debug("Request to get Thing1 : {}", id);
        return thing1Repository.findById(id)
            .map(thing1Mapper::toDto);
    }

    /**
     * Delete the thing1 by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Thing1 : {}", id);
        thing1Repository.deleteById(id);
        thing1SearchRepository.deleteById(id);
    }

    /**
     * Search for the thing1 corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<Thing1DTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Thing1s for query {}", query);
        return thing1SearchRepository.search(queryStringQuery(query), pageable)
            .map(thing1Mapper::toDto);
    }
}
