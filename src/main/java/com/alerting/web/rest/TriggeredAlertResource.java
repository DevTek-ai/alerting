package com.alerting.web.rest;

import com.alerting.domain.TriggeredAlert;
import com.alerting.repository.TriggeredAlertRepository;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alerting.domain.TriggeredAlert}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TriggeredAlertResource {

    private final Logger log = LoggerFactory.getLogger(TriggeredAlertResource.class);

    private static final String ENTITY_NAME = "alertingTriggeredAlert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TriggeredAlertRepository triggeredAlertRepository;

    public TriggeredAlertResource(TriggeredAlertRepository triggeredAlertRepository) {
        this.triggeredAlertRepository = triggeredAlertRepository;
    }

    /**
     * {@code POST  /triggered-alerts} : Create a new triggeredAlert.
     *
     * @param triggeredAlert the triggeredAlert to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new triggeredAlert, or with status {@code 400 (Bad Request)} if the triggeredAlert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/triggered-alerts")
    public ResponseEntity<TriggeredAlert> createTriggeredAlert(@RequestBody TriggeredAlert triggeredAlert) throws URISyntaxException {
        log.debug("REST request to save TriggeredAlert : {}", triggeredAlert);
        if (triggeredAlert.getId() != null) {
            throw new BadRequestAlertException("A new triggeredAlert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TriggeredAlert result = triggeredAlertRepository.save(triggeredAlert);
        return ResponseEntity.created(new URI("/api/triggered-alerts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /triggered-alerts} : Updates an existing triggeredAlert.
     *
     * @param triggeredAlert the triggeredAlert to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated triggeredAlert,
     * or with status {@code 400 (Bad Request)} if the triggeredAlert is not valid,
     * or with status {@code 500 (Internal Server Error)} if the triggeredAlert couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/triggered-alerts")
    public ResponseEntity<TriggeredAlert> updateTriggeredAlert(@RequestBody TriggeredAlert triggeredAlert) throws URISyntaxException {
        log.debug("REST request to update TriggeredAlert : {}", triggeredAlert);
        if (triggeredAlert.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TriggeredAlert result = triggeredAlertRepository.save(triggeredAlert);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, triggeredAlert.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /triggered-alerts} : get all the triggeredAlerts.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of triggeredAlerts in body.
     */
    @GetMapping("/triggered-alerts")
    public List<TriggeredAlert> getAllTriggeredAlerts() {
        log.debug("REST request to get all TriggeredAlerts");
        return triggeredAlertRepository.findAll();
    }

    /**
     * {@code GET  /triggered-alerts/:id} : get the "id" triggeredAlert.
     *
     * @param id the id of the triggeredAlert to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the triggeredAlert, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/triggered-alerts/{id}")
    public ResponseEntity<TriggeredAlert> getTriggeredAlert(@PathVariable Long id) {
        log.debug("REST request to get TriggeredAlert : {}", id);
        Optional<TriggeredAlert> triggeredAlert = triggeredAlertRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(triggeredAlert);
    }

    /**
     * {@code DELETE  /triggered-alerts/:id} : delete the "id" triggeredAlert.
     *
     * @param id the id of the triggeredAlert to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/triggered-alerts/{id}")
    public ResponseEntity<Void> deleteTriggeredAlert(@PathVariable Long id) {
        log.debug("REST request to delete TriggeredAlert : {}", id);
        triggeredAlertRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
