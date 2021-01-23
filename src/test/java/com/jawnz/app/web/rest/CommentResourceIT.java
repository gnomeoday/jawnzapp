package com.jawnz.app.web.rest;

import com.jawnz.app.JawnzappApp;
import com.jawnz.app.config.TestSecurityConfiguration;
import com.jawnz.app.domain.Comment;
import com.jawnz.app.repository.CommentRepository;
import com.jawnz.app.repository.search.CommentSearchRepository;
import com.jawnz.app.service.CommentService;

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

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.jawnz.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CommentResource} REST controller.
 */
@SpringBootTest(classes = { JawnzappApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class CommentResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    /**
     * This repository is mocked in the com.jawnz.app.repository.search test package.
     *
     * @see com.jawnz.app.repository.search.CommentSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommentSearchRepository mockCommentSearchRepository;

    @Autowired
    private MockMvc restCommentMockMvc;

    private Comment comment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comment createEntity() {
        Comment comment = new Comment()
            .text(DEFAULT_TEXT)
            .created(DEFAULT_CREATED);
        return comment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comment createUpdatedEntity() {
        Comment comment = new Comment()
            .text(UPDATED_TEXT)
            .created(UPDATED_CREATED);
        return comment;
    }

    @BeforeEach
    public void initTest() {
        commentRepository.deleteAll();
        comment = createEntity();
    }

    @Test
    public void createComment() throws Exception {
        int databaseSizeBeforeCreate = commentRepository.findAll().size();
        // Create the Comment
        restCommentMockMvc.perform(post("/api/comments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comment)))
            .andExpect(status().isCreated());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate + 1);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testComment.getCreated()).isEqualTo(DEFAULT_CREATED);

        // Validate the Comment in Elasticsearch
        verify(mockCommentSearchRepository, times(1)).save(testComment);
    }

    @Test
    public void createCommentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commentRepository.findAll().size();

        // Create the Comment with an existing ID
        comment.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentMockMvc.perform(post("/api/comments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comment)))
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Comment in Elasticsearch
        verify(mockCommentSearchRepository, times(0)).save(comment);
    }


    @Test
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentRepository.findAll().size();
        // set the field null
        comment.setText(null);

        // Create the Comment, which fails.


        restCommentMockMvc.perform(post("/api/comments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comment)))
            .andExpect(status().isBadRequest());

        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllComments() throws Exception {
        // Initialize the database
        commentRepository.save(comment);

        // Get all the commentList
        restCommentMockMvc.perform(get("/api/comments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comment.getId())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }
    
    @Test
    public void getComment() throws Exception {
        // Initialize the database
        commentRepository.save(comment);

        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", comment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comment.getId()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }
    @Test
    public void getNonExistingComment() throws Exception {
        // Get the comment
        restCommentMockMvc.perform(get("/api/comments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateComment() throws Exception {
        // Initialize the database
        commentService.save(comment);

        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // Update the comment
        Comment updatedComment = commentRepository.findById(comment.getId()).get();
        updatedComment
            .text(UPDATED_TEXT)
            .created(UPDATED_CREATED);

        restCommentMockMvc.perform(put("/api/comments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedComment)))
            .andExpect(status().isOk());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);
        Comment testComment = commentList.get(commentList.size() - 1);
        assertThat(testComment.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testComment.getCreated()).isEqualTo(UPDATED_CREATED);

        // Validate the Comment in Elasticsearch
        verify(mockCommentSearchRepository, times(2)).save(testComment);
    }

    @Test
    public void updateNonExistingComment() throws Exception {
        int databaseSizeBeforeUpdate = commentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentMockMvc.perform(put("/api/comments").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(comment)))
            .andExpect(status().isBadRequest());

        // Validate the Comment in the database
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Comment in Elasticsearch
        verify(mockCommentSearchRepository, times(0)).save(comment);
    }

    @Test
    public void deleteComment() throws Exception {
        // Initialize the database
        commentService.save(comment);

        int databaseSizeBeforeDelete = commentRepository.findAll().size();

        // Delete the comment
        restCommentMockMvc.perform(delete("/api/comments/{id}", comment.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Comment in Elasticsearch
        verify(mockCommentSearchRepository, times(1)).deleteById(comment.getId());
    }

    @Test
    public void searchComment() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        commentService.save(comment);
        when(mockCommentSearchRepository.search(queryStringQuery("id:" + comment.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(comment), PageRequest.of(0, 1), 1));

        // Search the comment
        restCommentMockMvc.perform(get("/api/_search/comments?query=id:" + comment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comment.getId())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }
}
