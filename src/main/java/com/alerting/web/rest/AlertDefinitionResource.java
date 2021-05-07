package com.alerting.web.rest;

import com.alerting.domain.AlertDefinition;
import com.alerting.repository.AlertDefinitionRepository;
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
 * REST controller for managing {@link com.alerting.domain.AlertDefinition}.
 */
@RestController
@RequestMapping("/alert/api")
@Transactional
public class AlertDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(AlertDefinitionResource.class);

    private static final String ENTITY_NAME = "alertingAlertDefinition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlertDefinitionRepository alertDefinitionRepository;

    public AlertDefinitionResource(AlertDefinitionRepository alertDefinitionRepository) {
        this.alertDefinitionRepository = alertDefinitionRepository;
    }

    /**
     * {@code POST  /alert-definitions} : Create a new alertDefinition.
     *
     * @param alertDefinition the alertDefinition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alertDefinition, or with status {@code 400 (Bad Request)} if the alertDefinition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alert-definitions")
    public ResponseEntity<AlertDefinition> createAlertDefinition(@RequestBody AlertDefinition alertDefinition) throws URISyntaxException {
        log.debug("REST request to save AlertDefinition : {}", alertDefinition);
        if (alertDefinition.getId() != null) {
            throw new BadRequestAlertException("A new alertDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alertDefinition.setDateCreated(Instant.now());
    if(alertDefinition.getTypeSelection().equals("Project") && alertDefinition.getAttributeSelection().equals("Status")){
        alertDefinition.setAttributeSelection("projectStatusId");
        alertDefinition.setAlertRuleQuery(alertDefinition.getAlertRuleQuery().replace("status","projectStatusId"));
    }
        if(alertDefinition.getTypeSelection().equals("Transaction")){
            alertDefinition.setTypeSelection("ChangeOrder");

        }
        AlertDefinition result = alertDefinitionRepository.save(alertDefinition);
        return ResponseEntity.created(new URI("/api/alert-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alert-definitions} : Updates an existing alertDefinition.
     *
     * @param alertDefinition the alertDefinition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alertDefinition,
     * or with status {@code 400 (Bad Request)} if the alertDefinition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alertDefinition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alert-definitions")
    public ResponseEntity<AlertDefinition> updateAlertDefinition(@RequestBody AlertDefinition alertDefinition) throws URISyntaxException {
        log.debug("REST request to update AlertDefinition : {}", alertDefinition);
        if (alertDefinition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        alertDefinition.setDateUpdated(Instant.now());
        if(alertDefinition.getTypeSelection().equals("Project") && alertDefinition.getAttributeSelection().equals("Status")){
            alertDefinition.setAttributeSelection("projectStatusId");
            alertDefinition.setAlertRuleQuery(alertDefinition.getAlertRuleQuery().replace("status","projectStatusId"));
        }
        if(alertDefinition.getTypeSelection().equals("Transaction")){
            alertDefinition.getTypeSelection().equals("ChangeOrder");
        }
        AlertDefinition result = alertDefinitionRepository.save(alertDefinition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alertDefinition.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /alert-definitions} : get all the alertDefinitions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alertDefinitions in body.
     */
    @GetMapping("/alert-definitions")
    public List<AlertDefinition> getAllAlertDefinitions() {
        log.debug("REST request to get all AlertDefinitions");
        return alertDefinitionRepository.findAll();
    }

    /**
     * {@code GET  /alert-definitions/:id} : get the "id" alertDefinition.
     *
     * @param id the id of the alertDefinition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alertDefinition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alert-definitions/{id}")
    public ResponseEntity<AlertDefinition> getAlertDefinition(@PathVariable Long id) {
        log.debug("REST request to get AlertDefinition : {}", id);
        Optional<AlertDefinition> alertDefinition = alertDefinitionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(alertDefinition);
    }

    /**
     * {@code DELETE  /alert-definitions/:id} : delete the "id" alertDefinition.
     *
     * @param id the id of the alertDefinition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alert-definitions/{id}")
    public ResponseEntity<Void> deleteAlertDefinition(@PathVariable Long id) {
        log.debug("REST request to delete AlertDefinition : {}", id);
        alertDefinitionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/ping")
    public  String ping() {

        return "hello";
    }

}
