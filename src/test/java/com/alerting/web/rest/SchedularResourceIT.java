package com.alerting.web.rest;

import com.alerting.AlertingApp;
import com.alerting.domain.Schedular;
import com.alerting.repository.SchedularRepository;
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

/**
 * Integration tests for the {@link SchedularResource} REST controller.
 */
@SpringBootTest(classes = AlertingApp.class)
public class SchedularResourceIT {

    private static final String DEFAULT_START_TIME = "AAAAAAAAAA";
    private static final String UPDATED_START_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_END_TIME = "AAAAAAAAAA";
    private static final String UPDATED_END_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_TRIGGERED_TIME = "AAAAAAAAAA";
    private static final String UPDATED_TRIGGERED_TIME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    @Autowired
    private SchedularRepository schedularRepository;

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

    private MockMvc restSchedularMockMvc;

    private Schedular schedular;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SchedularResource schedularResource = new SchedularResource(schedularRepository);
        this.restSchedularMockMvc = MockMvcBuilders.standaloneSetup(schedularResource)
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
    public static Schedular createEntity(EntityManager em) {
        Schedular schedular = new Schedular()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .triggeredTime(DEFAULT_TRIGGERED_TIME)
            .dateCreated(DEFAULT_DATE_CREATED)
            .createdBy(DEFAULT_CREATED_BY);
        return schedular;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schedular createUpdatedEntity(EntityManager em) {
        Schedular schedular = new Schedular()
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .triggeredTime(UPDATED_TRIGGERED_TIME)
            .dateCreated(UPDATED_DATE_CREATED)
            .createdBy(UPDATED_CREATED_BY);
        return schedular;
    }

    @BeforeEach
    public void initTest() {
        schedular = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchedular() throws Exception {
        int databaseSizeBeforeCreate = schedularRepository.findAll().size();

        // Create the Schedular
        restSchedularMockMvc.perform(post("/api/schedulars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedular)))
            .andExpect(status().isCreated());

        // Validate the Schedular in the database
        List<Schedular> schedularList = schedularRepository.findAll();
        assertThat(schedularList).hasSize(databaseSizeBeforeCreate + 1);
        Schedular testSchedular = schedularList.get(schedularList.size() - 1);
        assertThat(testSchedular.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testSchedular.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testSchedular.getTriggeredTime()).isEqualTo(DEFAULT_TRIGGERED_TIME);
        assertThat(testSchedular.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testSchedular.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void createSchedularWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = schedularRepository.findAll().size();

        // Create the Schedular with an existing ID
        schedular.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSchedularMockMvc.perform(post("/api/schedulars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedular)))
            .andExpect(status().isBadRequest());

        // Validate the Schedular in the database
        List<Schedular> schedularList = schedularRepository.findAll();
        assertThat(schedularList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = schedularRepository.findAll().size();
        // set the field null
        schedular.setDateCreated(null);

        // Create the Schedular, which fails.

        restSchedularMockMvc.perform(post("/api/schedulars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedular)))
            .andExpect(status().isBadRequest());

        List<Schedular> schedularList = schedularRepository.findAll();
        assertThat(schedularList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSchedulars() throws Exception {
        // Initialize the database
        schedularRepository.saveAndFlush(schedular);

        // Get all the schedularList
        restSchedularMockMvc.perform(get("/api/schedulars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedular.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.[*].triggeredTime").value(hasItem(DEFAULT_TRIGGERED_TIME)))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }
    
    @Test
    @Transactional
    public void getSchedular() throws Exception {
        // Initialize the database
        schedularRepository.saveAndFlush(schedular);

        // Get the schedular
        restSchedularMockMvc.perform(get("/api/schedulars/{id}", schedular.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schedular.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME))
            .andExpect(jsonPath("$.triggeredTime").value(DEFAULT_TRIGGERED_TIME))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    public void getNonExistingSchedular() throws Exception {
        // Get the schedular
        restSchedularMockMvc.perform(get("/api/schedulars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchedular() throws Exception {
        // Initialize the database
        schedularRepository.saveAndFlush(schedular);

        int databaseSizeBeforeUpdate = schedularRepository.findAll().size();

        // Update the schedular
        Schedular updatedSchedular = schedularRepository.findById(schedular.getId()).get();
        // Disconnect from session so that the updates on updatedSchedular are not directly saved in db
        em.detach(updatedSchedular);
        updatedSchedular
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .triggeredTime(UPDATED_TRIGGERED_TIME)
            .dateCreated(UPDATED_DATE_CREATED)
            .createdBy(UPDATED_CREATED_BY);

        restSchedularMockMvc.perform(put("/api/schedulars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSchedular)))
            .andExpect(status().isOk());

        // Validate the Schedular in the database
        List<Schedular> schedularList = schedularRepository.findAll();
        assertThat(schedularList).hasSize(databaseSizeBeforeUpdate);
        Schedular testSchedular = schedularList.get(schedularList.size() - 1);
        assertThat(testSchedular.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testSchedular.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testSchedular.getTriggeredTime()).isEqualTo(UPDATED_TRIGGERED_TIME);
        assertThat(testSchedular.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testSchedular.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void updateNonExistingSchedular() throws Exception {
        int databaseSizeBeforeUpdate = schedularRepository.findAll().size();

        // Create the Schedular

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSchedularMockMvc.perform(put("/api/schedulars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedular)))
            .andExpect(status().isBadRequest());

        // Validate the Schedular in the database
        List<Schedular> schedularList = schedularRepository.findAll();
        assertThat(schedularList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSchedular() throws Exception {
        // Initialize the database
        schedularRepository.saveAndFlush(schedular);

        int databaseSizeBeforeDelete = schedularRepository.findAll().size();

        // Delete the schedular
        restSchedularMockMvc.perform(delete("/api/schedulars/{id}", schedular.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Schedular> schedularList = schedularRepository.findAll();
        assertThat(schedularList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
