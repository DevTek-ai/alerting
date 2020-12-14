package com.alerting.web.rest;

import com.alerting.domain.AlertHistory;
import com.alerting.repository.AlertHistoryRepository;
import com.alerting.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alerting.domain.AlertHistory}.
 */
@RestController
@RequestMapping("/alert/api")
@Transactional
public class AlertHistoryResource {

    private final Logger log = LoggerFactory.getLogger(AlertHistoryResource.class);

    private static final String ENTITY_NAME = "alertingAlertHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlertHistoryRepository alertHistoryRepository;

    public AlertHistoryResource(AlertHistoryRepository alertHistoryRepository) {
        this.alertHistoryRepository = alertHistoryRepository;
    }

    /**
     * {@code POST  /alert-histories} : Create a new alertHistory.
     *
     * @param alertHistory the alertHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alertHistory, or with status {@code 400 (Bad Request)} if the alertHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alert-histories")
    public ResponseEntity<AlertHistory> createAlertHistory(@RequestBody AlertHistory alertHistory) throws URISyntaxException {
        log.debug("REST request to save AlertHistory : {}", alertHistory);
        if (alertHistory.getId() != null) {
            throw new BadRequestAlertException("A new alertHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alertHistory.setDateCreated(Instant.now());
        AlertHistory result = alertHistoryRepository.save(alertHistory);
        return ResponseEntity.created(new URI("/api/alert-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alert-histories} : Updates an existing alertHistory.
     *
     * @param alertHistory the alertHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alertHistory,
     * or with status {@code 400 (Bad Request)} if the alertHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alertHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alert-histories")
    public ResponseEntity<AlertHistory> updateAlertHistory(@RequestBody AlertHistory alertHistory) throws URISyntaxException {
        log.debug("REST request to update AlertHistory : {}", alertHistory);
        if (alertHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        alertHistory.setDateModified(Instant.now());
        AlertHistory result = alertHistoryRepository.save(alertHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alertHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /alert-histories} : get all the alertHistories.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alertHistories in body.
     */
    @GetMapping("/alert-histories")
    public List<AlertHistory> getAllAlertHistories() {
        log.debug("REST request to get all AlertHistories");
        return alertHistoryRepository.findAll();
    }

    /**
     * {@code GET  /alert-histories/:id} : get the "id" alertHistory.
     *
     * @param id the id of the alertHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alertHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alert-histories/{id}")
    public ResponseEntity<AlertHistory> getAlertHistory(@PathVariable Long id) {
        log.debug("REST request to get AlertHistory : {}", id);
        Optional<AlertHistory> alertHistory = alertHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(alertHistory);
    }

    /**
     * {@code DELETE  /alert-histories/:id} : delete the "id" alertHistory.
     *
     * @param id the id of the alertHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alert-histories/{id}")
    public ResponseEntity<Void> deleteAlertHistory(@PathVariable Long id) {
        log.debug("REST request to delete AlertHistory : {}", id);
        alertHistoryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
