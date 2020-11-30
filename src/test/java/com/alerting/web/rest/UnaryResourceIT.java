package com.alerting.web.rest;

import com.alerting.AlertingApp;
import com.alerting.domain.Unary;
import com.alerting.repository.UnaryRepository;
import com.alerting.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.alerting.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UnaryResource} REST controller.
 */
@SpringBootTest(classes = AlertingApp.class)
public class UnaryResourceIT {

    @Autowired
    private UnaryRepository unaryRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUnaryMockMvc;

    private Unary unary;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UnaryResource unaryResource = new UnaryResource(unaryRepository);
        this.restUnaryMockMvc = MockMvcBuilders.standaloneSetup(unaryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unary createEntity(EntityManager em) {
        Unary unary = new Unary();
        return unary;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unary createUpdatedEntity(EntityManager em) {
        Unary unary = new Unary();
        return unary;
    }

    @BeforeEach
    public void initTest() {
        unary = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnary() throws Exception {
        int databaseSizeBeforeCreate = unaryRepository.findAll().size();

        // Create the Unary
        restUnaryMockMvc.perform(post("/api/unaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unary)))
            .andExpect(status().isCreated());

        // Validate the Unary in the database
        List<Unary> unaryList = unaryRepository.findAll();
        assertThat(unaryList).hasSize(databaseSizeBeforeCreate + 1);
        Unary testUnary = unaryList.get(unaryList.size() - 1);
    }

    @Test
    @Transactional
    public void createUnaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unaryRepository.findAll().size();

        // Create the Unary with an existing ID
        unary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnaryMockMvc.perform(post("/api/unaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unary)))
            .andExpect(status().isBadRequest());

        // Validate the Unary in the database
        List<Unary> unaryList = unaryRepository.findAll();
        assertThat(unaryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUnaries() throws Exception {
        // Initialize the database
        unaryRepository.saveAndFlush(unary);

        // Get all the unaryList
        restUnaryMockMvc.perform(get("/api/unaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unary.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getUnary() throws Exception {
        // Initialize the database
        unaryRepository.saveAndFlush(unary);

        // Get the unary
        restUnaryMockMvc.perform(get("/api/unaries/{id}", unary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unary.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUnary() throws Exception {
        // Get the unary
        restUnaryMockMvc.perform(get("/api/unaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnary() throws Exception {
        // Initialize the database
        unaryRepository.saveAndFlush(unary);

        int databaseSizeBeforeUpdate = unaryRepository.findAll().size();

        // Update the unary
        Unary updatedUnary = unaryRepository.findById(unary.getId()).get();
        // Disconnect from session so that the updates on updatedUnary are not directly saved in db
        em.detach(updatedUnary);

        restUnaryMockMvc.perform(put("/api/unaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUnary)))
            .andExpect(status().isOk());

        // Validate the Unary in the database
        List<Unary> unaryList = unaryRepository.findAll();
        assertThat(unaryList).hasSize(databaseSizeBeforeUpdate);
        Unary testUnary = unaryList.get(unaryList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingUnary() throws Exception {
        int databaseSizeBeforeUpdate = unaryRepository.findAll().size();

        // Create the Unary

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnaryMockMvc.perform(put("/api/unaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unary)))
            .andExpect(status().isBadRequest());

        // Validate the Unary in the database
        List<Unary> unaryList = unaryRepository.findAll();
        assertThat(unaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUnary() throws Exception {
        // Initialize the database
        unaryRepository.saveAndFlush(unary);

        int databaseSizeBeforeDelete = unaryRepository.findAll().size();

        // Delete the unary
        restUnaryMockMvc.perform(delete("/api/unaries/{id}", unary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Unary> unaryList = unaryRepository.findAll();
        assertThat(unaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
