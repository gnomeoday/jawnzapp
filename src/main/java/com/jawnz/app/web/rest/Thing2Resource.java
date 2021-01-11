package com.jawnz.app.web.rest;

import com.jawnz.app.service.Thing2Service;
import com.jawnz.app.web.rest.errors.BadRequestAlertException;
import com.jawnz.app.service.dto.Thing2DTO;

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
 * REST controller for managing {@link com.jawnz.app.domain.Thing2}.
 */
@RestController
@RequestMapping("/api")
public class Thing2Resource {

    private final Logger log = LoggerFactory.getLogger(Thing2Resource.class);

    private static final String ENTITY_NAME = "jawnzappThing2";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Thing2Service thing2Service;

    public Thing2Resource(Thing2Service thing2Service) {
        this.thing2Service = thing2Service;
    }

    /**
     * {@code POST  /thing-2-s} : Create a new thing2.
     *
     * @param thing2DTO the thing2DTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new thing2DTO, or with status {@code 400 (Bad Request)} if the thing2 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/thing-2-s")
    public ResponseEntity<Thing2DTO> createThing2(@RequestBody Thing2DTO thing2DTO) throws URISyntaxException {
        log.debug("REST request to save Thing2 : {}", thing2DTO);
        if (thing2DTO.getId() != null) {
            throw new BadRequestAlertException("A new thing2 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Thing2DTO result = thing2Service.save(thing2DTO);
        return ResponseEntity.created(new URI("/api/thing-2-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /thing-2-s} : Updates an existing thing2.
     *
     * @param thing2DTO the thing2DTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated thing2DTO,
     * or with status {@code 400 (Bad Request)} if the thing2DTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the thing2DTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/thing-2-s")
    public ResponseEntity<Thing2DTO> updateThing2(@RequestBody Thing2DTO thing2DTO) throws URISyntaxException {
        log.debug("REST request to update Thing2 : {}", thing2DTO);
        if (thing2DTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Thing2DTO result = thing2Service.save(thing2DTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, thing2DTO.getId()))
            .body(result);
    }

    /**
     * {@code GET  /thing-2-s} : get all the thing2s.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of thing2s in body.
     */
    @GetMapping("/thing-2-s")
    public ResponseEntity<List<Thing2DTO>> getAllThing2s(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("child-is-null".equals(filter)) {
            log.debug("REST request to get all Thing2s where child is null");
            return new ResponseEntity<>(thing2Service.findAllWhereChildIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Thing2s");
        Page<Thing2DTO> page = thing2Service.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /thing-2-s/:id} : get the "id" thing2.
     *
     * @param id the id of the thing2DTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the thing2DTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/thing-2-s/{id}")
    public ResponseEntity<Thing2DTO> getThing2(@PathVariable String id) {
        log.debug("REST request to get Thing2 : {}", id);
        Optional<Thing2DTO> thing2DTO = thing2Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(thing2DTO);
    }

    /**
     * {@code DELETE  /thing-2-s/:id} : delete the "id" thing2.
     *
     * @param id the id of the thing2DTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/thing-2-s/{id}")
    public ResponseEntity<Void> deleteThing2(@PathVariable String id) {
        log.debug("REST request to delete Thing2 : {}", id);
        thing2Service.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/thing-2-s?query=:query} : search for the thing2 corresponding
     * to the query.
     *
     * @param query the query of the thing2 search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/thing-2-s")
    public ResponseEntity<List<Thing2DTO>> searchThing2s(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Thing2s for query {}", query);
        Page<Thing2DTO> page = thing2Service.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
