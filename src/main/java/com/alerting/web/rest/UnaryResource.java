package com.alerting.web.rest;

import com.alerting.domain.Unary;
import com.alerting.repository.UnaryRepository;
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
 * REST controller for managing {@link com.alerting.domain.Unary}.
 */
@RestController
@RequestMapping("/alert/api")
@Transactional
public class UnaryResource {

    private final Logger log = LoggerFactory.getLogger(UnaryResource.class);

    private static final String ENTITY_NAME = "alertingUnary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnaryRepository unaryRepository;

    public UnaryResource(UnaryRepository unaryRepository) {
        this.unaryRepository = unaryRepository;
    }

    /**
     * {@code POST  /unaries} : Create a new unary.
     *
     * @param unary the unary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unary, or with status {@code 400 (Bad Request)} if the unary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unaries")
    public ResponseEntity<Unary> createUnary(@RequestBody Unary unary) throws URISyntaxException {
        log.debug("REST request to save Unary : {}", unary);
        if (unary.getId() != null) {
            throw new BadRequestAlertException("A new unary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Unary result = unaryRepository.save(unary);
        return ResponseEntity.created(new URI("/api/unaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unaries} : Updates an existing unary.
     *
     * @param unary the unary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unary,
     * or with status {@code 400 (Bad Request)} if the unary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unaries")
    public ResponseEntity<Unary> updateUnary(@RequestBody Unary unary) throws URISyntaxException {
        log.debug("REST request to update Unary : {}", unary);
        if (unary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Unary result = unaryRepository.save(unary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, unary.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /unaries} : get all the unaries.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unaries in body.
     */
    @GetMapping("/unaries")
    public List<Unary> getAllUnaries(@RequestParam(required = false) String filter) {
        if ("operator-is-null".equals(filter)) {
            log.debug("REST request to get all Unarys where operator is null");
            return StreamSupport
                .stream(unaryRepository.findAll().spliterator(), false)
                .filter(unary -> unary.getOperator() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Unaries");
        return unaryRepository.findAll();
    }

    /**
     * {@code GET  /unaries/:id} : get the "id" unary.
     *
     * @param id the id of the unary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unaries/{id}")
    public ResponseEntity<Unary> getUnary(@PathVariable Long id) {
        log.debug("REST request to get Unary : {}", id);
        Optional<Unary> unary = unaryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(unary);
    }

    /**
     * {@code DELETE  /unaries/:id} : delete the "id" unary.
     *
     * @param id the id of the unary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unaries/{id}")
    public ResponseEntity<Void> deleteUnary(@PathVariable Long id) {
        log.debug("REST request to delete Unary : {}", id);
        unaryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
