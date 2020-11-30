package com.alerting.web.rest;

import com.alerting.AlertingApp;
import com.alerting.domain.AlertDefinition;
import com.alerting.repository.AlertDefinitionRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.alerting.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alerting.domain.enumeration.TriggerType;
import com.alerting.domain.enumeration.Category;
/**
 * Integration tests for the {@link AlertDefinitionResource} REST controller.
 */
@SpringBootTest(classes = AlertingApp.class)
public class AlertDefinitionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final TriggerType DEFAULT_TRIGGER_TYPE = TriggerType.IMMEDIATE;
    private static final TriggerType UPDATED_TRIGGER_TYPE = TriggerType.SCHEDULE;

    private static final Boolean DEFAULT_NOTIFY = false;
    private static final Boolean UPDATED_NOTIFY = true;

    private static final Category DEFAULT_CATEGORY = Category.WARNING;
    private static final Category UPDATED_CATEGORY = Category.INFO;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AlertDefinitionRepository alertDefinitionRepository;

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

    private MockMvc restAlertDefinitionMockMvc;

    private AlertDefinition alertDefinition;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlertDefinitionResource alertDefinitionResource = new AlertDefinitionResource(alertDefinitionRepository);
        this.restAlertDefinitionMockMvc = MockMvcBuilders.standaloneSetup(alertDefinitionResource)
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
    public static AlertDefinition createEntity(EntityManager em) {
        AlertDefinition alertDefinition = new AlertDefinition()
            .title(DEFAULT_TITLE)
            .message(DEFAULT_MESSAGE)
            .triggerType(DEFAULT_TRIGGER_TYPE)
            .notify(DEFAULT_NOTIFY)
            .category(DEFAULT_CATEGORY)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED);
        return alertDefinition;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertDefinition createUpdatedEntity(EntityManager em) {
        AlertDefinition alertDefinition = new AlertDefinition()
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .triggerType(UPDATED_TRIGGER_TYPE)
            .notify(UPDATED_NOTIFY)
            .category(UPDATED_CATEGORY)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);
        return alertDefinition;
    }

    @BeforeEach
    public void initTest() {
        alertDefinition = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlertDefinition() throws Exception {
        int databaseSizeBeforeCreate = alertDefinitionRepository.findAll().size();

        // Create the AlertDefinition
        restAlertDefinitionMockMvc.perform(post("/api/alert-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alertDefinition)))
            .andExpect(status().isCreated());

        // Validate the AlertDefinition in the database
        List<AlertDefinition> alertDefinitionList = alertDefinitionRepository.findAll();
        assertThat(alertDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        AlertDefinition testAlertDefinition = alertDefinitionList.get(alertDefinitionList.size() - 1);
        assertThat(testAlertDefinition.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAlertDefinition.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testAlertDefinition.getTriggerType()).isEqualTo(DEFAULT_TRIGGER_TYPE);
        assertThat(testAlertDefinition.isNotify()).isEqualTo(DEFAULT_NOTIFY);
        assertThat(testAlertDefinition.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testAlertDefinition.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testAlertDefinition.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void createAlertDefinitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alertDefinitionRepository.findAll().size();

        // Create the AlertDefinition with an existing ID
        alertDefinition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertDefinitionMockMvc.perform(post("/api/alert-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alertDefinition)))
            .andExpect(status().isBadRequest());

        // Validate the AlertDefinition in the database
        List<AlertDefinition> alertDefinitionList = alertDefinitionRepository.findAll();
        assertThat(alertDefinitionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAlertDefinitions() throws Exception {
        // Initialize the database
        alertDefinitionRepository.saveAndFlush(alertDefinition);

        // Get all the alertDefinitionList
        restAlertDefinitionMockMvc.perform(get("/api/alert-definitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alertDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].triggerType").value(hasItem(DEFAULT_TRIGGER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notify").value(hasItem(DEFAULT_NOTIFY.booleanValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getAlertDefinition() throws Exception {
        // Initialize the database
        alertDefinitionRepository.saveAndFlush(alertDefinition);

        // Get the alertDefinition
        restAlertDefinitionMockMvc.perform(get("/api/alert-definitions/{id}", alertDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alertDefinition.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.triggerType").value(DEFAULT_TRIGGER_TYPE.toString()))
            .andExpect(jsonPath("$.notify").value(DEFAULT_NOTIFY.booleanValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAlertDefinition() throws Exception {
        // Get the alertDefinition
        restAlertDefinitionMockMvc.perform(get("/api/alert-definitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlertDefinition() throws Exception {
        // Initialize the database
        alertDefinitionRepository.saveAndFlush(alertDefinition);

        int databaseSizeBeforeUpdate = alertDefinitionRepository.findAll().size();

        // Update the alertDefinition
        AlertDefinition updatedAlertDefinition = alertDefinitionRepository.findById(alertDefinition.getId()).get();
        // Disconnect from session so that the updates on updatedAlertDefinition are not directly saved in db
        em.detach(updatedAlertDefinition);
        updatedAlertDefinition
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .triggerType(UPDATED_TRIGGER_TYPE)
            .notify(UPDATED_NOTIFY)
            .category(UPDATED_CATEGORY)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED);

        restAlertDefinitionMockMvc.perform(put("/api/alert-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAlertDefinition)))
            .andExpect(status().isOk());

        // Validate the AlertDefinition in the database
        List<AlertDefinition> alertDefinitionList = alertDefinitionRepository.findAll();
        assertThat(alertDefinitionList).hasSize(databaseSizeBeforeUpdate);
        AlertDefinition testAlertDefinition = alertDefinitionList.get(alertDefinitionList.size() - 1);
        assertThat(testAlertDefinition.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAlertDefinition.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testAlertDefinition.getTriggerType()).isEqualTo(UPDATED_TRIGGER_TYPE);
        assertThat(testAlertDefinition.isNotify()).isEqualTo(UPDATED_NOTIFY);
        assertThat(testAlertDefinition.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testAlertDefinition.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testAlertDefinition.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingAlertDefinition() throws Exception {
        int databaseSizeBeforeUpdate = alertDefinitionRepository.findAll().size();

        // Create the AlertDefinition

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertDefinitionMockMvc.perform(put("/api/alert-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alertDefinition)))
            .andExpect(status().isBadRequest());

        // Validate the AlertDefinition in the database
        List<AlertDefinition> alertDefinitionList = alertDefinitionRepository.findAll();
        assertThat(alertDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlertDefinition() throws Exception {
        // Initialize the database
        alertDefinitionRepository.saveAndFlush(alertDefinition);

        int databaseSizeBeforeDelete = alertDefinitionRepository.findAll().size();

        // Delete the alertDefinition
        restAlertDefinitionMockMvc.perform(delete("/api/alert-definitions/{id}", alertDefinition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AlertDefinition> alertDefinitionList = alertDefinitionRepository.findAll();
        assertThat(alertDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
