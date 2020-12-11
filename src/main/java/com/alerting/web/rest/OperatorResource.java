package com.alerting.web.rest;

import com.alerting.domain.Operator;
import com.alerting.repository.OperatorRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.alerting.domain.Operator}.
 */
@RestController
@RequestMapping("/alert/api")
@Transactional
public class OperatorResource {

    private final Logger log = LoggerFactory.getLogger(OperatorResource.class);

    private static final String ENTITY_NAME = "alertingOperator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperatorRepository operatorRepository;

    public OperatorResource(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    /**
     * {@code POST  /operators} : Create a new operator.
     *
     * @param operator the operator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operator, or with status {@code 400 (Bad Request)} if the operator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operators")
    public ResponseEntity<Operator> createOperator(@RequestBody Operator operator) throws URISyntaxException {
        log.debug("REST request to save Operator : {}", operator);
        if (operator.getId() != null) {
            throw new BadRequestAlertException("A new operator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Operator result = operatorRepository.save(operator);
        return ResponseEntity.created(new URI("/api/operators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operators} : Updates an existing operator.
     *
     * @param operator the operator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operator,
     * or with status {@code 400 (Bad Request)} if the operator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operators")
    public ResponseEntity<Operator> updateOperator(@RequestBody Operator operator) throws URISyntaxException {
        log.debug("REST request to update Operator : {}", operator);
        if (operator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Operator result = operatorRepository.save(operator);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, operator.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /operators} : get all the operators.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operators in body.
     */
    @GetMapping("/operators")
    public List<Operator> getAllOperators(@RequestParam(required = false) String filter) {
        if ("statement-is-null".equals(filter)) {
            log.debug("REST request to get all Operators where statement is null");
            return StreamSupport
                .stream(operatorRepository.findAll().spliterator(), false)
                .filter(operator -> operator.getStatement() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Operators");
        return operatorRepository.findAll();
    }

    /**
     * {@code GET  /operators/:id} : get the "id" operator.
     *
     * @param id the id of the operator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operators/{id}")
    public ResponseEntity<Operator> getOperator(@PathVariable Long id) {
        log.debug("REST request to get Operator : {}", id);
        Optional<Operator> operator = operatorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(operator);
    }

    /**
     * {@code DELETE  /operators/:id} : delete the "id" operator.
     *
     * @param id the id of the operator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operators/{id}")
    public ResponseEntity<Void> deleteOperator(@PathVariable Long id) {
        log.debug("REST request to delete Operator : {}", id);
        operatorRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
