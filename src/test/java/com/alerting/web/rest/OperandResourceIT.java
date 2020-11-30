package com.alerting.web.rest;

import com.alerting.AlertingApp;
import com.alerting.domain.Operand;
import com.alerting.repository.OperandRepository;
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
 * Integration tests for the {@link OperandResource} REST controller.
 */
@SpringBootTest(classes = AlertingApp.class)
public class OperandResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private OperandRepository operandRepository;

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

    private MockMvc restOperandMockMvc;

    private Operand operand;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OperandResource operandResource = new OperandResource(operandRepository);
        this.restOperandMockMvc = MockMvcBuilders.standaloneSetup(operandResource)
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
    public static Operand createEntity(EntityManager em) {
        Operand operand = new Operand()
            .type(DEFAULT_TYPE);
        return operand;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operand createUpdatedEntity(EntityManager em) {
        Operand operand = new Operand()
            .type(UPDATED_TYPE);
        return operand;
    }

    @BeforeEach
    public void initTest() {
        operand = createEntity(em);
    }

    @Test
    @Transactional
    public void createOperand() throws Exception {
        int databaseSizeBeforeCreate = operandRepository.findAll().size();

        // Create the Operand
        restOperandMockMvc.perform(post("/api/operands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operand)))
            .andExpect(status().isCreated());

        // Validate the Operand in the database
        List<Operand> operandList = operandRepository.findAll();
        assertThat(operandList).hasSize(databaseSizeBeforeCreate + 1);
        Operand testOperand = operandList.get(operandList.size() - 1);
        assertThat(testOperand.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createOperandWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = operandRepository.findAll().size();

        // Create the Operand with an existing ID
        operand.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperandMockMvc.perform(post("/api/operands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operand)))
            .andExpect(status().isBadRequest());

        // Validate the Operand in the database
        List<Operand> operandList = operandRepository.findAll();
        assertThat(operandList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOperands() throws Exception {
        // Initialize the database
        operandRepository.saveAndFlush(operand);

        // Get all the operandList
        restOperandMockMvc.perform(get("/api/operands?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operand.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getOperand() throws Exception {
        // Initialize the database
        operandRepository.saveAndFlush(operand);

        // Get the operand
        restOperandMockMvc.perform(get("/api/operands/{id}", operand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(operand.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingOperand() throws Exception {
        // Get the operand
        restOperandMockMvc.perform(get("/api/operands/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOperand() throws Exception {
        // Initialize the database
        operandRepository.saveAndFlush(operand);

        int databaseSizeBeforeUpdate = operandRepository.findAll().size();

        // Update the operand
        Operand updatedOperand = operandRepository.findById(operand.getId()).get();
        // Disconnect from session so that the updates on updatedOperand are not directly saved in db
        em.detach(updatedOperand);
        updatedOperand
            .type(UPDATED_TYPE);

        restOperandMockMvc.perform(put("/api/operands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOperand)))
            .andExpect(status().isOk());

        // Validate the Operand in the database
        List<Operand> operandList = operandRepository.findAll();
        assertThat(operandList).hasSize(databaseSizeBeforeUpdate);
        Operand testOperand = operandList.get(operandList.size() - 1);
        assertThat(testOperand.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingOperand() throws Exception {
        int databaseSizeBeforeUpdate = operandRepository.findAll().size();

        // Create the Operand

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperandMockMvc.perform(put("/api/operands")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operand)))
            .andExpect(status().isBadRequest());

        // Validate the Operand in the database
        List<Operand> operandList = operandRepository.findAll();
        assertThat(operandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOperand() throws Exception {
        // Initialize the database
        operandRepository.saveAndFlush(operand);

        int databaseSizeBeforeDelete = operandRepository.findAll().size();

        // Delete the operand
        restOperandMockMvc.perform(delete("/api/operands/{id}", operand.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operand> operandList = operandRepository.findAll();
        assertThat(operandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
