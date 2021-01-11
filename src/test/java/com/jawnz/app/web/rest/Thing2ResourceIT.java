package com.jawnz.app.web.rest;

import com.jawnz.app.JawnzappApp;
import com.jawnz.app.config.TestSecurityConfiguration;
import com.jawnz.app.domain.Thing2;
import com.jawnz.app.repository.Thing2Repository;
import com.jawnz.app.repository.search.Thing2SearchRepository;
import com.jawnz.app.service.Thing2Service;
import com.jawnz.app.service.dto.Thing2DTO;
import com.jawnz.app.service.mapper.Thing2Mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link Thing2Resource} REST controller.
 */
@SpringBootTest(classes = { JawnzappApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class Thing2ResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private Thing2Repository thing2Repository;

    @Autowired
    private Thing2Mapper thing2Mapper;

    @Autowired
    private Thing2Service thing2Service;

    /**
     * This repository is mocked in the com.jawnz.app.repository.search test package.
     *
     * @see com.jawnz.app.repository.search.Thing2SearchRepositoryMockConfiguration
     */
    @Autowired
    private Thing2SearchRepository mockThing2SearchRepository;

    @Autowired
    private MockMvc restThing2MockMvc;

    private Thing2 thing2;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thing2 createEntity() {
        Thing2 thing2 = new Thing2()
            .name(DEFAULT_NAME);
        return thing2;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thing2 createUpdatedEntity() {
        Thing2 thing2 = new Thing2()
            .name(UPDATED_NAME);
        return thing2;
    }

    @BeforeEach
    public void initTest() {
        thing2Repository.deleteAll();
        thing2 = createEntity();
    }

    @Test
    public void createThing2() throws Exception {
        int databaseSizeBeforeCreate = thing2Repository.findAll().size();
        // Create the Thing2
        Thing2DTO thing2DTO = thing2Mapper.toDto(thing2);
        restThing2MockMvc.perform(post("/api/thing-2-s").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thing2DTO)))
            .andExpect(status().isCreated());

        // Validate the Thing2 in the database
        List<Thing2> thing2List = thing2Repository.findAll();
        assertThat(thing2List).hasSize(databaseSizeBeforeCreate + 1);
        Thing2 testThing2 = thing2List.get(thing2List.size() - 1);
        assertThat(testThing2.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Thing2 in Elasticsearch
        verify(mockThing2SearchRepository, times(1)).save(testThing2);
    }

    @Test
    public void createThing2WithExistingId() throws Exception {
        int databaseSizeBeforeCreate = thing2Repository.findAll().size();

        // Create the Thing2 with an existing ID
        thing2.setId("existing_id");
        Thing2DTO thing2DTO = thing2Mapper.toDto(thing2);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThing2MockMvc.perform(post("/api/thing-2-s").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thing2DTO)))
            .andExpect(status().isBadRequest());

        // Validate the Thing2 in the database
        List<Thing2> thing2List = thing2Repository.findAll();
        assertThat(thing2List).hasSize(databaseSizeBeforeCreate);

        // Validate the Thing2 in Elasticsearch
        verify(mockThing2SearchRepository, times(0)).save(thing2);
    }


    @Test
    public void getAllThing2s() throws Exception {
        // Initialize the database
        thing2Repository.save(thing2);

        // Get all the thing2List
        restThing2MockMvc.perform(get("/api/thing-2-s?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thing2.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    public void getThing2() throws Exception {
        // Initialize the database
        thing2Repository.save(thing2);

        // Get the thing2
        restThing2MockMvc.perform(get("/api/thing-2-s/{id}", thing2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(thing2.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    public void getNonExistingThing2() throws Exception {
        // Get the thing2
        restThing2MockMvc.perform(get("/api/thing-2-s/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateThing2() throws Exception {
        // Initialize the database
        thing2Repository.save(thing2);

        int databaseSizeBeforeUpdate = thing2Repository.findAll().size();

        // Update the thing2
        Thing2 updatedThing2 = thing2Repository.findById(thing2.getId()).get();
        updatedThing2
            .name(UPDATED_NAME);
        Thing2DTO thing2DTO = thing2Mapper.toDto(updatedThing2);

        restThing2MockMvc.perform(put("/api/thing-2-s").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thing2DTO)))
            .andExpect(status().isOk());

        // Validate the Thing2 in the database
        List<Thing2> thing2List = thing2Repository.findAll();
        assertThat(thing2List).hasSize(databaseSizeBeforeUpdate);
        Thing2 testThing2 = thing2List.get(thing2List.size() - 1);
        assertThat(testThing2.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Thing2 in Elasticsearch
        verify(mockThing2SearchRepository, times(1)).save(testThing2);
    }

    @Test
    public void updateNonExistingThing2() throws Exception {
        int databaseSizeBeforeUpdate = thing2Repository.findAll().size();

        // Create the Thing2
        Thing2DTO thing2DTO = thing2Mapper.toDto(thing2);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThing2MockMvc.perform(put("/api/thing-2-s").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thing2DTO)))
            .andExpect(status().isBadRequest());

        // Validate the Thing2 in the database
        List<Thing2> thing2List = thing2Repository.findAll();
        assertThat(thing2List).hasSize(databaseSizeBeforeUpdate);

        // Validate the Thing2 in Elasticsearch
        verify(mockThing2SearchRepository, times(0)).save(thing2);
    }

    @Test
    public void deleteThing2() throws Exception {
        // Initialize the database
        thing2Repository.save(thing2);

        int databaseSizeBeforeDelete = thing2Repository.findAll().size();

        // Delete the thing2
        restThing2MockMvc.perform(delete("/api/thing-2-s/{id}", thing2.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Thing2> thing2List = thing2Repository.findAll();
        assertThat(thing2List).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Thing2 in Elasticsearch
        verify(mockThing2SearchRepository, times(1)).deleteById(thing2.getId());
    }

    @Test
    public void searchThing2() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        thing2Repository.save(thing2);
        when(mockThing2SearchRepository.search(queryStringQuery("id:" + thing2.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(thing2), PageRequest.of(0, 1), 1));

        // Search the thing2
        restThing2MockMvc.perform(get("/api/_search/thing-2-s?query=id:" + thing2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thing2.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
