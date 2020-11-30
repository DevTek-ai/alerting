package com.alerting.web.rest;

import com.alerting.AlertingApp;
import com.alerting.domain.Binary;
import com.alerting.repository.BinaryRepository;
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
 * Integration tests for the {@link BinaryResource} REST controller.
 */
@SpringBootTest(classes = AlertingApp.class)
public class BinaryResourceIT {

    @Autowired
    private BinaryRepository binaryRepository;

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

    private MockMvc restBinaryMockMvc;

    private Binary binary;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BinaryResource binaryResource = new BinaryResource(binaryRepository);
        this.restBinaryMockMvc = MockMvcBuilders.standaloneSetup(binaryResource)
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
    public static Binary createEntity(EntityManager em) {
        Binary binary = new Binary();
        return binary;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Binary createUpdatedEntity(EntityManager em) {
        Binary binary = new Binary();
        return binary;
    }

    @BeforeEach
    public void initTest() {
        binary = createEntity(em);
    }

    @Test
    @Transactional
    public void createBinary() throws Exception {
        int databaseSizeBeforeCreate = binaryRepository.findAll().size();

        // Create the Binary
        restBinaryMockMvc.perform(post("/api/binaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(binary)))
            .andExpect(status().isCreated());

        // Validate the Binary in the database
        List<Binary> binaryList = binaryRepository.findAll();
        assertThat(binaryList).hasSize(databaseSizeBeforeCreate + 1);
        Binary testBinary = binaryList.get(binaryList.size() - 1);
    }

    @Test
    @Transactional
    public void createBinaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = binaryRepository.findAll().size();

        // Create the Binary with an existing ID
        binary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBinaryMockMvc.perform(post("/api/binaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(binary)))
            .andExpect(status().isBadRequest());

        // Validate the Binary in the database
        List<Binary> binaryList = binaryRepository.findAll();
        assertThat(binaryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBinaries() throws Exception {
        // Initialize the database
        binaryRepository.saveAndFlush(binary);

        // Get all the binaryList
        restBinaryMockMvc.perform(get("/api/binaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(binary.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getBinary() throws Exception {
        // Initialize the database
        binaryRepository.saveAndFlush(binary);

        // Get the binary
        restBinaryMockMvc.perform(get("/api/binaries/{id}", binary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(binary.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBinary() throws Exception {
        // Get the binary
        restBinaryMockMvc.perform(get("/api/binaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBinary() throws Exception {
        // Initialize the database
        binaryRepository.saveAndFlush(binary);

        int databaseSizeBeforeUpdate = binaryRepository.findAll().size();

        // Update the binary
        Binary updatedBinary = binaryRepository.findById(binary.getId()).get();
        // Disconnect from session so that the updates on updatedBinary are not directly saved in db
        em.detach(updatedBinary);

        restBinaryMockMvc.perform(put("/api/binaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBinary)))
            .andExpect(status().isOk());

        // Validate the Binary in the database
        List<Binary> binaryList = binaryRepository.findAll();
        assertThat(binaryList).hasSize(databaseSizeBeforeUpdate);
        Binary testBinary = binaryList.get(binaryList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingBinary() throws Exception {
        int databaseSizeBeforeUpdate = binaryRepository.findAll().size();

        // Create the Binary

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBinaryMockMvc.perform(put("/api/binaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(binary)))
            .andExpect(status().isBadRequest());

        // Validate the Binary in the database
        List<Binary> binaryList = binaryRepository.findAll();
        assertThat(binaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBinary() throws Exception {
        // Initialize the database
        binaryRepository.saveAndFlush(binary);

        int databaseSizeBeforeDelete = binaryRepository.findAll().size();

        // Delete the binary
        restBinaryMockMvc.perform(delete("/api/binaries/{id}", binary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Binary> binaryList = binaryRepository.findAll();
        assertThat(binaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
