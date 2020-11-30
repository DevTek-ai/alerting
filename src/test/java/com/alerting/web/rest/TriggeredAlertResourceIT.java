package com.alerting.web.rest;

import com.alerting.AlertingApp;
import com.alerting.domain.TriggeredAlert;
import com.alerting.repository.TriggeredAlertRepository;
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

import com.alerting.domain.enumeration.TriggeredStatus;
/**
 * Integration tests for the {@link TriggeredAlertResource} REST controller.
 */
@SpringBootTest(classes = AlertingApp.class)
public class TriggeredAlertResourceIT {

    private static final TriggeredStatus DEFAULT_STATUS = TriggeredStatus.ACTIVE;
    private static final TriggeredStatus UPDATED_STATUS = TriggeredStatus.INACTIVE;

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TARGET_URL = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_URL = "BBBBBBBBBB";

    @Autowired
    private TriggeredAlertRepository triggeredAlertRepository;

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

    private MockMvc restTriggeredAlertMockMvc;

    private TriggeredAlert triggeredAlert;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TriggeredAlertResource triggeredAlertResource = new TriggeredAlertResource(triggeredAlertRepository);
        this.restTriggeredAlertMockMvc = MockMvcBuilders.standaloneSetup(triggeredAlertResource)
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
    public static TriggeredAlert createEntity(EntityManager em) {
        TriggeredAlert triggeredAlert = new TriggeredAlert()
            .status(DEFAULT_STATUS)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateUpdated(DEFAULT_DATE_UPDATED)
            .targetUrl(DEFAULT_TARGET_URL);
        return triggeredAlert;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TriggeredAlert createUpdatedEntity(EntityManager em) {
        TriggeredAlert triggeredAlert = new TriggeredAlert()
            .status(UPDATED_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .targetUrl(UPDATED_TARGET_URL);
        return triggeredAlert;
    }

    @BeforeEach
    public void initTest() {
        triggeredAlert = createEntity(em);
    }

    @Test
    @Transactional
    public void createTriggeredAlert() throws Exception {
        int databaseSizeBeforeCreate = triggeredAlertRepository.findAll().size();

        // Create the TriggeredAlert
        restTriggeredAlertMockMvc.perform(post("/api/triggered-alerts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(triggeredAlert)))
            .andExpect(status().isCreated());

        // Validate the TriggeredAlert in the database
        List<TriggeredAlert> triggeredAlertList = triggeredAlertRepository.findAll();
        assertThat(triggeredAlertList).hasSize(databaseSizeBeforeCreate + 1);
        TriggeredAlert testTriggeredAlert = triggeredAlertList.get(triggeredAlertList.size() - 1);
        assertThat(testTriggeredAlert.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTriggeredAlert.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testTriggeredAlert.getDateUpdated()).isEqualTo(DEFAULT_DATE_UPDATED);
        assertThat(testTriggeredAlert.getTargetUrl()).isEqualTo(DEFAULT_TARGET_URL);
    }

    @Test
    @Transactional
    public void createTriggeredAlertWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = triggeredAlertRepository.findAll().size();

        // Create the TriggeredAlert with an existing ID
        triggeredAlert.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTriggeredAlertMockMvc.perform(post("/api/triggered-alerts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(triggeredAlert)))
            .andExpect(status().isBadRequest());

        // Validate the TriggeredAlert in the database
        List<TriggeredAlert> triggeredAlertList = triggeredAlertRepository.findAll();
        assertThat(triggeredAlertList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTriggeredAlerts() throws Exception {
        // Initialize the database
        triggeredAlertRepository.saveAndFlush(triggeredAlert);

        // Get all the triggeredAlertList
        restTriggeredAlertMockMvc.perform(get("/api/triggered-alerts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(triggeredAlert.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateUpdated").value(hasItem(DEFAULT_DATE_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].targetUrl").value(hasItem(DEFAULT_TARGET_URL)));
    }
    
    @Test
    @Transactional
    public void getTriggeredAlert() throws Exception {
        // Initialize the database
        triggeredAlertRepository.saveAndFlush(triggeredAlert);

        // Get the triggeredAlert
        restTriggeredAlertMockMvc.perform(get("/api/triggered-alerts/{id}", triggeredAlert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(triggeredAlert.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateUpdated").value(DEFAULT_DATE_UPDATED.toString()))
            .andExpect(jsonPath("$.targetUrl").value(DEFAULT_TARGET_URL));
    }

    @Test
    @Transactional
    public void getNonExistingTriggeredAlert() throws Exception {
        // Get the triggeredAlert
        restTriggeredAlertMockMvc.perform(get("/api/triggered-alerts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTriggeredAlert() throws Exception {
        // Initialize the database
        triggeredAlertRepository.saveAndFlush(triggeredAlert);

        int databaseSizeBeforeUpdate = triggeredAlertRepository.findAll().size();

        // Update the triggeredAlert
        TriggeredAlert updatedTriggeredAlert = triggeredAlertRepository.findById(triggeredAlert.getId()).get();
        // Disconnect from session so that the updates on updatedTriggeredAlert are not directly saved in db
        em.detach(updatedTriggeredAlert);
        updatedTriggeredAlert
            .status(UPDATED_STATUS)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateUpdated(UPDATED_DATE_UPDATED)
            .targetUrl(UPDATED_TARGET_URL);

        restTriggeredAlertMockMvc.perform(put("/api/triggered-alerts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTriggeredAlert)))
            .andExpect(status().isOk());

        // Validate the TriggeredAlert in the database
        List<TriggeredAlert> triggeredAlertList = triggeredAlertRepository.findAll();
        assertThat(triggeredAlertList).hasSize(databaseSizeBeforeUpdate);
        TriggeredAlert testTriggeredAlert = triggeredAlertList.get(triggeredAlertList.size() - 1);
        assertThat(testTriggeredAlert.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTriggeredAlert.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testTriggeredAlert.getDateUpdated()).isEqualTo(UPDATED_DATE_UPDATED);
        assertThat(testTriggeredAlert.getTargetUrl()).isEqualTo(UPDATED_TARGET_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingTriggeredAlert() throws Exception {
        int databaseSizeBeforeUpdate = triggeredAlertRepository.findAll().size();

        // Create the TriggeredAlert

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTriggeredAlertMockMvc.perform(put("/api/triggered-alerts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(triggeredAlert)))
            .andExpect(status().isBadRequest());

        // Validate the TriggeredAlert in the database
        List<TriggeredAlert> triggeredAlertList = triggeredAlertRepository.findAll();
        assertThat(triggeredAlertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTriggeredAlert() throws Exception {
        // Initialize the database
        triggeredAlertRepository.saveAndFlush(triggeredAlert);

        int databaseSizeBeforeDelete = triggeredAlertRepository.findAll().size();

        // Delete the triggeredAlert
        restTriggeredAlertMockMvc.perform(delete("/api/triggered-alerts/{id}", triggeredAlert.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TriggeredAlert> triggeredAlertList = triggeredAlertRepository.findAll();
        assertThat(triggeredAlertList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
