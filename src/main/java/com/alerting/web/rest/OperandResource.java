package com.alerting.web.rest;

import com.alerting.domain.Operand;
import com.alerting.repository.OperandRepository;
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
 * REST controller for managing {@link com.alerting.domain.Operand}.
 */
@RestController
@RequestMapping("/alert/api")
@Transactional
public class OperandResource {

    private final Logger log = LoggerFactory.getLogger(OperandResource.class);

    private static final String ENTITY_NAME = "alertingOperand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperandRepository operandRepository;

    public OperandResource(OperandRepository operandRepository) {
        this.operandRepository = operandRepository;
    }

    /**
     * {@code POST  /operands} : Create a new operand.
     *
     * @param operand the operand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operand, or with status {@code 400 (Bad Request)} if the operand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operands")
    public ResponseEntity<Operand> createOperand(@RequestBody Operand operand) throws URISyntaxException {
        log.debug("REST request to save Operand : {}", operand);
        if (operand.getId() != null) {
            throw new BadRequestAlertException("A new operand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Operand result = operandRepository.save(operand);
        return ResponseEntity.created(new URI("/api/operands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operands} : Updates an existing operand.
     *
     * @param operand the operand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operand,
     * or with status {@code 400 (Bad Request)} if the operand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operands")
    public ResponseEntity<Operand> updateOperand(@RequestBody Operand operand) throws URISyntaxException {
        log.debug("REST request to update Operand : {}", operand);
        if (operand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Operand result = operandRepository.save(operand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, operand.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /operands} : get all the operands.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operands in body.
     */
    @GetMapping("/operands")
    public List<Operand> getAllOperands(@RequestParam(required = false) String filter) {
        if ("statement-is-null".equals(filter)) {
            log.debug("REST request to get all Operands where statement is null");
            return StreamSupport
                .stream(operandRepository.findAll().spliterator(), false)
                .filter(operand -> operand.getStatement() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Operands");
        return operandRepository.findAll();
    }

    /**
     * {@code GET  /operands/:id} : get the "id" operand.
     *
     * @param id the id of the operand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operands/{id}")
    public ResponseEntity<Operand> getOperand(@PathVariable Long id) {
        log.debug("REST request to get Operand : {}", id);
        Optional<Operand> operand = operandRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(operand);
    }

    /**
     * {@code DELETE  /operands/:id} : delete the "id" operand.
     *
     * @param id the id of the operand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operands/{id}")
    public ResponseEntity<Void> deleteOperand(@PathVariable Long id) {
        log.debug("REST request to delete Operand : {}", id);
        operandRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
