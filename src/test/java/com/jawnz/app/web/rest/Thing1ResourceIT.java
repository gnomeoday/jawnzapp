package com.jawnz.app.web.rest;

import com.jawnz.app.JawnzappApp;
import com.jawnz.app.config.TestSecurityConfiguration;
import com.jawnz.app.domain.Thing1;
import com.jawnz.app.repository.Thing1Repository;
import com.jawnz.app.repository.search.Thing1SearchRepository;
import com.jawnz.app.service.Thing1Service;
import com.jawnz.app.service.dto.Thing1DTO;
import com.jawnz.app.service.mapper.Thing1Mapper;

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
 * Integration tests for the {@link Thing1Resource} REST controller.
 */
@SpringBootTest(classes = { JawnzappApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class Thing1ResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    @Autowired
    private Thing1Repository thing1Repository;

    @Autowired
    private Thing1Mapper thing1Mapper;

    @Autowired
    private Thing1Service thing1Service;

    /**
     * This repository is mocked in the com.jawnz.app.repository.search test package.
     *
     * @see com.jawnz.app.repository.search.Thing1SearchRepositoryMockConfiguration
     */
    @Autowired
    private Thing1SearchRepository mockThing1SearchRepository;

    @Autowired
    private MockMvc restThing1MockMvc;

    private Thing1 thing1;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thing1 createEntity() {
        Thing1 thing1 = new Thing1()
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE);
        return thing1;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Thing1 createUpdatedEntity() {
        Thing1 thing1 = new Thing1()
            .name(UPDATED_NAME)
            .age(UPDATED_AGE);
        return thing1;
    }

    @BeforeEach
    public void initTest() {
        thing1Repository.deleteAll();
        thing1 = createEntity();
    }

    @Test
    public void createThing1() throws Exception {
        int databaseSizeBeforeCreate = thing1Repository.findAll().size();
        // Create the Thing1
        Thing1DTO thing1DTO = thing1Mapper.toDto(thing1);
        restThing1MockMvc.perform(post("/api/thing-1-s").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thing1DTO)))
            .andExpect(status().isCreated());

        // Validate the Thing1 in the database
        List<Thing1> thing1List = thing1Repository.findAll();
        assertThat(thing1List).hasSize(databaseSizeBeforeCreate + 1);
        Thing1 testThing1 = thing1List.get(thing1List.size() - 1);
        assertThat(testThing1.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testThing1.getAge()).isEqualTo(DEFAULT_AGE);

        // Validate the Thing1 in Elasticsearch
        verify(mockThing1SearchRepository, times(1)).save(testThing1);
    }

    @Test
    public void createThing1WithExistingId() throws Exception {
        int databaseSizeBeforeCreate = thing1Repository.findAll().size();

        // Create the Thing1 with an existing ID
        thing1.setId("existing_id");
        Thing1DTO thing1DTO = thing1Mapper.toDto(thing1);

        // An entity with an existing ID cannot be created, so this API call must fail
        restThing1MockMvc.perform(post("/api/thing-1-s").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thing1DTO)))
            .andExpect(status().isBadRequest());

        // Validate the Thing1 in the database
        List<Thing1> thing1List = thing1Repository.findAll();
        assertThat(thing1List).hasSize(databaseSizeBeforeCreate);

        // Validate the Thing1 in Elasticsearch
        verify(mockThing1SearchRepository, times(0)).save(thing1);
    }


    @Test
    public void getAllThing1s() throws Exception {
        // Initialize the database
        thing1Repository.save(thing1);

        // Get all the thing1List
        restThing1MockMvc.perform(get("/api/thing-1-s?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thing1.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }
    
    @Test
    public void getThing1() throws Exception {
        // Initialize the database
        thing1Repository.save(thing1);

        // Get the thing1
        restThing1MockMvc.perform(get("/api/thing-1-s/{id}", thing1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(thing1.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }
    @Test
    public void getNonExistingThing1() throws Exception {
        // Get the thing1
        restThing1MockMvc.perform(get("/api/thing-1-s/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateThing1() throws Exception {
        // Initialize the database
        thing1Repository.save(thing1);

        int databaseSizeBeforeUpdate = thing1Repository.findAll().size();

        // Update the thing1
        Thing1 updatedThing1 = thing1Repository.findById(thing1.getId()).get();
        updatedThing1
            .name(UPDATED_NAME)
            .age(UPDATED_AGE);
        Thing1DTO thing1DTO = thing1Mapper.toDto(updatedThing1);

        restThing1MockMvc.perform(put("/api/thing-1-s").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thing1DTO)))
            .andExpect(status().isOk());

        // Validate the Thing1 in the database
        List<Thing1> thing1List = thing1Repository.findAll();
        assertThat(thing1List).hasSize(databaseSizeBeforeUpdate);
        Thing1 testThing1 = thing1List.get(thing1List.size() - 1);
        assertThat(testThing1.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testThing1.getAge()).isEqualTo(UPDATED_AGE);

        // Validate the Thing1 in Elasticsearch
        verify(mockThing1SearchRepository, times(1)).save(testThing1);
    }

    @Test
    public void updateNonExistingThing1() throws Exception {
        int databaseSizeBeforeUpdate = thing1Repository.findAll().size();

        // Create the Thing1
        Thing1DTO thing1DTO = thing1Mapper.toDto(thing1);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restThing1MockMvc.perform(put("/api/thing-1-s").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(thing1DTO)))
            .andExpect(status().isBadRequest());

        // Validate the Thing1 in the database
        List<Thing1> thing1List = thing1Repository.findAll();
        assertThat(thing1List).hasSize(databaseSizeBeforeUpdate);

        // Validate the Thing1 in Elasticsearch
        verify(mockThing1SearchRepository, times(0)).save(thing1);
    }

    @Test
    public void deleteThing1() throws Exception {
        // Initialize the database
        thing1Repository.save(thing1);

        int databaseSizeBeforeDelete = thing1Repository.findAll().size();

        // Delete the thing1
        restThing1MockMvc.perform(delete("/api/thing-1-s/{id}", thing1.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Thing1> thing1List = thing1Repository.findAll();
        assertThat(thing1List).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Thing1 in Elasticsearch
        verify(mockThing1SearchRepository, times(1)).deleteById(thing1.getId());
    }

    @Test
    public void searchThing1() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        thing1Repository.save(thing1);
        when(mockThing1SearchRepository.search(queryStringQuery("id:" + thing1.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(thing1), PageRequest.of(0, 1), 1));

        // Search the thing1
        restThing1MockMvc.perform(get("/api/_search/thing-1-s?query=id:" + thing1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(thing1.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }
}
