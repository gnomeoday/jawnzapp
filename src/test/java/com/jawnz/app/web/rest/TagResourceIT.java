package com.jawnz.app.web.rest;

import com.jawnz.app.JawnzappApp;
import com.jawnz.app.config.TestSecurityConfiguration;
import com.jawnz.app.domain.Tag;
import com.jawnz.app.repository.TagRepository;
import com.jawnz.app.repository.search.TagSearchRepository;
import com.jawnz.app.service.TagService;

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
 * Integration tests for the {@link TagResource} REST controller.
 */
@SpringBootTest(classes = { JawnzappApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TagResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagService tagService;

    /**
     * This repository is mocked in the com.jawnz.app.repository.search test package.
     *
     * @see com.jawnz.app.repository.search.TagSearchRepositoryMockConfiguration
     */
    @Autowired
    private TagSearchRepository mockTagSearchRepository;

    @Autowired
    private MockMvc restTagMockMvc;

    private Tag tag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createEntity() {
        Tag tag = new Tag()
            .text(DEFAULT_TEXT);
        return tag;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createUpdatedEntity() {
        Tag tag = new Tag()
            .text(UPDATED_TEXT);
        return tag;
    }

    @BeforeEach
    public void initTest() {
        tagRepository.deleteAll();
        tag = createEntity();
    }

    @Test
    public void createTag() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();
        // Create the Tag
        restTagMockMvc.perform(post("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tag)))
            .andExpect(status().isCreated());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getText()).isEqualTo(DEFAULT_TEXT);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(1)).save(testTag);
    }

    @Test
    public void createTagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // Create the Tag with an existing ID
        tag.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMockMvc.perform(post("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tag)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(0)).save(tag);
    }


    @Test
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setText(null);

        // Create the Tag, which fails.


        restTagMockMvc.perform(post("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tag)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTags() throws Exception {
        // Initialize the database
        tagRepository.save(tag);

        // Get all the tagList
        restTagMockMvc.perform(get("/api/tags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }
    
    @Test
    public void getTag() throws Exception {
        // Initialize the database
        tagRepository.save(tag);

        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tag.getId()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT));
    }
    @Test
    public void getNonExistingTag() throws Exception {
        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTag() throws Exception {
        // Initialize the database
        tagService.save(tag);

        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag
        Tag updatedTag = tagRepository.findById(tag.getId()).get();
        updatedTag
            .text(UPDATED_TEXT);

        restTagMockMvc.perform(put("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTag)))
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getText()).isEqualTo(UPDATED_TEXT);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(2)).save(testTag);
    }

    @Test
    public void updateNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc.perform(put("/api/tags").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(tag)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(0)).save(tag);
    }

    @Test
    public void deleteTag() throws Exception {
        // Initialize the database
        tagService.save(tag);

        int databaseSizeBeforeDelete = tagRepository.findAll().size();

        // Delete the tag
        restTagMockMvc.perform(delete("/api/tags/{id}", tag.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Tag in Elasticsearch
        verify(mockTagSearchRepository, times(1)).deleteById(tag.getId());
    }

    @Test
    public void searchTag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        tagService.save(tag);
        when(mockTagSearchRepository.search(queryStringQuery("id:" + tag.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tag), PageRequest.of(0, 1), 1));

        // Search the tag
        restTagMockMvc.perform(get("/api/_search/tags?query=id:" + tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }
}
