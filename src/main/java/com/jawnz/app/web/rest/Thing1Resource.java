package com.jawnz.app.web.rest;

import com.jawnz.app.service.Thing1Service;
import com.jawnz.app.web.rest.errors.BadRequestAlertException;
import com.jawnz.app.service.dto.Thing1DTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.jawnz.app.domain.Thing1}.
 */
@RestController
@RequestMapping("/api")
public class Thing1Resource {

    private final Logger log = LoggerFactory.getLogger(Thing1Resource.class);

    private static final String ENTITY_NAME = "jawnzappThing1";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Thing1Service thing1Service;

    public Thing1Resource(Thing1Service thing1Service) {
        this.thing1Service = thing1Service;
    }

    /**
     * {@code POST  /thing-1-s} : Create a new thing1.
     *
     * @param thing1DTO the thing1DTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new thing1DTO, or with status {@code 400 (Bad Request)} if the thing1 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/thing-1-s")
    public ResponseEntity<Thing1DTO> createThing1(@RequestBody Thing1DTO thing1DTO) throws URISyntaxException {
        log.debug("REST request to save Thing1 : {}", thing1DTO);
        if (thing1DTO.getId() != null) {
            throw new BadRequestAlertException("A new thing1 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Thing1DTO result = thing1Service.save(thing1DTO);
        return ResponseEntity.created(new URI("/api/thing-1-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /thing-1-s} : Updates an existing thing1.
     *
     * @param thing1DTO the thing1DTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated thing1DTO,
     * or with status {@code 400 (Bad Request)} if the thing1DTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the thing1DTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/thing-1-s")
    public ResponseEntity<Thing1DTO> updateThing1(@RequestBody Thing1DTO thing1DTO) throws URISyntaxException {
        log.debug("REST request to update Thing1 : {}", thing1DTO);
        if (thing1DTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Thing1DTO result = thing1Service.save(thing1DTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, thing1DTO.getId()))
            .body(result);
    }

    /**
     * {@code GET  /thing-1-s} : get all the thing1s.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of thing1s in body.
     */
    @GetMapping("/thing-1-s")
    public ResponseEntity<List<Thing1DTO>> getAllThing1s(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("parent-is-null".equals(filter)) {
            log.debug("REST request to get all Thing1s where parent is null");
            return new ResponseEntity<>(thing1Service.findAllWhereParentIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Thing1s");
        Page<Thing1DTO> page = thing1Service.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /thing-1-s/:id} : get the "id" thing1.
     *
     * @param id the id of the thing1DTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the thing1DTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/thing-1-s/{id}")
    public ResponseEntity<Thing1DTO> getThing1(@PathVariable String id) {
        log.debug("REST request to get Thing1 : {}", id);
        Optional<Thing1DTO> thing1DTO = thing1Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(thing1DTO);
    }

    /**
     * {@code DELETE  /thing-1-s/:id} : delete the "id" thing1.
     *
     * @param id the id of the thing1DTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/thing-1-s/{id}")
    public ResponseEntity<Void> deleteThing1(@PathVariable String id) {
        log.debug("REST request to delete Thing1 : {}", id);
        thing1Service.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/thing-1-s?query=:query} : search for the thing1 corresponding
     * to the query.
     *
     * @param query the query of the thing1 search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/thing-1-s")
    public ResponseEntity<List<Thing1DTO>> searchThing1s(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Thing1s for query {}", query);
        Page<Thing1DTO> page = thing1Service.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
