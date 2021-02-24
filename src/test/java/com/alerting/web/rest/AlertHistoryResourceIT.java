package com.alerting.web.rest;

import com.alerting.AlertingApp;
import com.alerting.domain.AlertHistory;
import com.alerting.repository.AlertHistoryRepository;
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
 * Integration tests for the {@link AlertHistoryResource} REST controller.
 */
@SpringBootTest(classes = AlertingApp.class)
public class AlertHistoryResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EMAIL_STATUS = false;
    private static final Boolean UPDATED_EMAIL_STATUS = true;

    private static final Boolean DEFAULT_WEB_SOCKET_STATUS = false;
    private static final Boolean UPDATED_WEB_SOCKET_STATUS = true;

    private static final Boolean DEFAULT_SMS_STATUS = false;
    private static final Boolean UPDATED_SMS_STATUS = true;

    private static final Boolean DEFAULT_WEB_SOCKECT_READ = false;
    private static final Boolean UPDATED_WEB_SOCKECT_READ = true;

    private static final Integer DEFAULT_RETRY_ATTEMPTS = 1;
    private static final Integer UPDATED_RETRY_ATTEMPTS = 2;

    private static final String DEFAULT_ERROR_LOG = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_LOG = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIPIENT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_RECEIPIENT_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    @Autowired
    private AlertHistoryRepository alertHistoryRepository;

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

    private MockMvc restAlertHistoryMockMvc;

    private AlertHistory alertHistory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlertHistoryResource alertHistoryResource = new AlertHistoryResource(alertHistoryRepository, alertGraphRepository);
        this.restAlertHistoryMockMvc = MockMvcBuilders.standaloneSetup(alertHistoryResource)
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
    public static AlertHistory createEntity(EntityManager em) {
        AlertHistory alertHistory = new AlertHistory()
            .subject(DEFAULT_SUBJECT)
            .message(DEFAULT_MESSAGE)
            .emailStatus(DEFAULT_EMAIL_STATUS)
            .webSocketStatus(DEFAULT_WEB_SOCKET_STATUS)
            .smsStatus(DEFAULT_SMS_STATUS)
            .webSockectRead(DEFAULT_WEB_SOCKECT_READ)
            .retryAttempts(DEFAULT_RETRY_ATTEMPTS)
            .errorLog(DEFAULT_ERROR_LOG)
            .receipientEmail(DEFAULT_RECEIPIENT_EMAIL)
            .userId(DEFAULT_USER_ID);
        return alertHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertHistory createUpdatedEntity(EntityManager em) {
        AlertHistory alertHistory = new AlertHistory()
            .subject(UPDATED_SUBJECT)
            .message(UPDATED_MESSAGE)
            .emailStatus(UPDATED_EMAIL_STATUS)
            .webSocketStatus(UPDATED_WEB_SOCKET_STATUS)
            .smsStatus(UPDATED_SMS_STATUS)
            .webSockectRead(UPDATED_WEB_SOCKECT_READ)
            .retryAttempts(UPDATED_RETRY_ATTEMPTS)
            .errorLog(UPDATED_ERROR_LOG)
            .receipientEmail(UPDATED_RECEIPIENT_EMAIL)
            .userId(UPDATED_USER_ID);
        return alertHistory;
    }

    @BeforeEach
    public void initTest() {
        alertHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlertHistory() throws Exception {
        int databaseSizeBeforeCreate = alertHistoryRepository.findAll().size();

        // Create the AlertHistory
        restAlertHistoryMockMvc.perform(post("/api/alert-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alertHistory)))
            .andExpect(status().isCreated());

        // Validate the AlertHistory in the database
        List<AlertHistory> alertHistoryList = alertHistoryRepository.findAll();
        assertThat(alertHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        AlertHistory testAlertHistory = alertHistoryList.get(alertHistoryList.size() - 1);
        assertThat(testAlertHistory.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testAlertHistory.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testAlertHistory.isEmailStatus()).isEqualTo(DEFAULT_EMAIL_STATUS);
        assertThat(testAlertHistory.isWebSocketStatus()).isEqualTo(DEFAULT_WEB_SOCKET_STATUS);
        assertThat(testAlertHistory.isSmsStatus()).isEqualTo(DEFAULT_SMS_STATUS);
        assertThat(testAlertHistory.isWebSockectRead()).isEqualTo(DEFAULT_WEB_SOCKECT_READ);
        assertThat(testAlertHistory.getRetryAttempts()).isEqualTo(DEFAULT_RETRY_ATTEMPTS);
        assertThat(testAlertHistory.getErrorLog()).isEqualTo(DEFAULT_ERROR_LOG);
        assertThat(testAlertHistory.getReceipientEmail()).isEqualTo(DEFAULT_RECEIPIENT_EMAIL);
        assertThat(testAlertHistory.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    public void createAlertHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = alertHistoryRepository.findAll().size();

        // Create the AlertHistory with an existing ID
        alertHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertHistoryMockMvc.perform(post("/api/alert-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alertHistory)))
            .andExpect(status().isBadRequest());

        // Validate the AlertHistory in the database
        List<AlertHistory> alertHistoryList = alertHistoryRepository.findAll();
        assertThat(alertHistoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAlertHistories() throws Exception {
        // Initialize the database
        alertHistoryRepository.saveAndFlush(alertHistory);

        // Get all the alertHistoryList
        restAlertHistoryMockMvc.perform(get("/api/alert-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alertHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].emailStatus").value(hasItem(DEFAULT_EMAIL_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].webSocketStatus").value(hasItem(DEFAULT_WEB_SOCKET_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].smsStatus").value(hasItem(DEFAULT_SMS_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].webSockectRead").value(hasItem(DEFAULT_WEB_SOCKECT_READ.booleanValue())))
            .andExpect(jsonPath("$.[*].retryAttempts").value(hasItem(DEFAULT_RETRY_ATTEMPTS)))
            .andExpect(jsonPath("$.[*].errorLog").value(hasItem(DEFAULT_ERROR_LOG)))
            .andExpect(jsonPath("$.[*].receipientEmail").value(hasItem(DEFAULT_RECEIPIENT_EMAIL)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getAlertHistory() throws Exception {
        // Initialize the database
        alertHistoryRepository.saveAndFlush(alertHistory);

        // Get the alertHistory
        restAlertHistoryMockMvc.perform(get("/api/alert-histories/{id}", alertHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(alertHistory.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.emailStatus").value(DEFAULT_EMAIL_STATUS.booleanValue()))
            .andExpect(jsonPath("$.webSocketStatus").value(DEFAULT_WEB_SOCKET_STATUS.booleanValue()))
            .andExpect(jsonPath("$.smsStatus").value(DEFAULT_SMS_STATUS.booleanValue()))
            .andExpect(jsonPath("$.webSockectRead").value(DEFAULT_WEB_SOCKECT_READ.booleanValue()))
            .andExpect(jsonPath("$.retryAttempts").value(DEFAULT_RETRY_ATTEMPTS))
            .andExpect(jsonPath("$.errorLog").value(DEFAULT_ERROR_LOG))
            .andExpect(jsonPath("$.receipientEmail").value(DEFAULT_RECEIPIENT_EMAIL))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAlertHistory() throws Exception {
        // Get the alertHistory
        restAlertHistoryMockMvc.perform(get("/api/alert-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlertHistory() throws Exception {
        // Initialize the database
        alertHistoryRepository.saveAndFlush(alertHistory);

        int databaseSizeBeforeUpdate = alertHistoryRepository.findAll().size();

        // Update the alertHistory
        AlertHistory updatedAlertHistory = alertHistoryRepository.findById(alertHistory.getId()).get();
        // Disconnect from session so that the updates on updatedAlertHistory are not directly saved in db
        em.detach(updatedAlertHistory);
        updatedAlertHistory
            .subject(UPDATED_SUBJECT)
            .message(UPDATED_MESSAGE)
            .emailStatus(UPDATED_EMAIL_STATUS)
            .webSocketStatus(UPDATED_WEB_SOCKET_STATUS)
            .smsStatus(UPDATED_SMS_STATUS)
            .webSockectRead(UPDATED_WEB_SOCKECT_READ)
            .retryAttempts(UPDATED_RETRY_ATTEMPTS)
            .errorLog(UPDATED_ERROR_LOG)
            .receipientEmail(UPDATED_RECEIPIENT_EMAIL)
            .userId(UPDATED_USER_ID);

        restAlertHistoryMockMvc.perform(put("/api/alert-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAlertHistory)))
            .andExpect(status().isOk());

        // Validate the AlertHistory in the database
        List<AlertHistory> alertHistoryList = alertHistoryRepository.findAll();
        assertThat(alertHistoryList).hasSize(databaseSizeBeforeUpdate);
        AlertHistory testAlertHistory = alertHistoryList.get(alertHistoryList.size() - 1);
        assertThat(testAlertHistory.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testAlertHistory.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testAlertHistory.isEmailStatus()).isEqualTo(UPDATED_EMAIL_STATUS);
        assertThat(testAlertHistory.isWebSocketStatus()).isEqualTo(UPDATED_WEB_SOCKET_STATUS);
        assertThat(testAlertHistory.isSmsStatus()).isEqualTo(UPDATED_SMS_STATUS);
        assertThat(testAlertHistory.isWebSockectRead()).isEqualTo(UPDATED_WEB_SOCKECT_READ);
        assertThat(testAlertHistory.getRetryAttempts()).isEqualTo(UPDATED_RETRY_ATTEMPTS);
        assertThat(testAlertHistory.getErrorLog()).isEqualTo(UPDATED_ERROR_LOG);
        assertThat(testAlertHistory.getReceipientEmail()).isEqualTo(UPDATED_RECEIPIENT_EMAIL);
        assertThat(testAlertHistory.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingAlertHistory() throws Exception {
        int databaseSizeBeforeUpdate = alertHistoryRepository.findAll().size();

        // Create the AlertHistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertHistoryMockMvc.perform(put("/api/alert-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(alertHistory)))
            .andExpect(status().isBadRequest());

        // Validate the AlertHistory in the database
        List<AlertHistory> alertHistoryList = alertHistoryRepository.findAll();
        assertThat(alertHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlertHistory() throws Exception {
        // Initialize the database
        alertHistoryRepository.saveAndFlush(alertHistory);

        int databaseSizeBeforeDelete = alertHistoryRepository.findAll().size();

        // Delete the alertHistory
        restAlertHistoryMockMvc.perform(delete("/api/alert-histories/{id}", alertHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AlertHistory> alertHistoryList = alertHistoryRepository.findAll();
        assertThat(alertHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
