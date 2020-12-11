package com.alerting.web.rest;

import com.alerting.domain.Binary;
import com.alerting.repository.BinaryRepository;
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
 * REST controller for managing {@link com.alerting.domain.Binary}.
 */
@RestController
@RequestMapping("/alert/api")
@Transactional
public class BinaryResource {

    private final Logger log = LoggerFactory.getLogger(BinaryResource.class);

    private static final String ENTITY_NAME = "alertingBinary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BinaryRepository binaryRepository;

    public BinaryResource(BinaryRepository binaryRepository) {
        this.binaryRepository = binaryRepository;
    }

    /**
     * {@code POST  /binaries} : Create a new binary.
     *
     * @param binary the binary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new binary, or with status {@code 400 (Bad Request)} if the binary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/binaries")
    public ResponseEntity<Binary> createBinary(@RequestBody Binary binary) throws URISyntaxException {
        log.debug("REST request to save Binary : {}", binary);
        if (binary.getId() != null) {
            throw new BadRequestAlertException("A new binary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Binary result = binaryRepository.save(binary);
        return ResponseEntity.created(new URI("/api/binaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /binaries} : Updates an existing binary.
     *
     * @param binary the binary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated binary,
     * or with status {@code 400 (Bad Request)} if the binary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the binary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/binaries")
    public ResponseEntity<Binary> updateBinary(@RequestBody Binary binary) throws URISyntaxException {
        log.debug("REST request to update Binary : {}", binary);
        if (binary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Binary result = binaryRepository.save(binary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, binary.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /binaries} : get all the binaries.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of binaries in body.
     */
    @GetMapping("/binaries")
    public List<Binary> getAllBinaries(@RequestParam(required = false) String filter) {
        if ("statement-is-null".equals(filter)) {
            log.debug("REST request to get all Binarys where statement is null");
            return StreamSupport
                .stream(binaryRepository.findAll().spliterator(), false)
                .filter(binary -> binary.getStatement() == null)
                .collect(Collectors.toList());
        }
        if ("operator-is-null".equals(filter)) {
            log.debug("REST request to get all Binarys where operator is null");
            return StreamSupport
                .stream(binaryRepository.findAll().spliterator(), false)
                .filter(binary -> binary.getOperator() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Binaries");
        return binaryRepository.findAll();
    }

    /**
     * {@code GET  /binaries/:id} : get the "id" binary.
     *
     * @param id the id of the binary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the binary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/binaries/{id}")
    public ResponseEntity<Binary> getBinary(@PathVariable Long id) {
        log.debug("REST request to get Binary : {}", id);
        Optional<Binary> binary = binaryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(binary);
    }

    /**
     * {@code DELETE  /binaries/:id} : delete the "id" binary.
     *
     * @param id the id of the binary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/binaries/{id}")
    public ResponseEntity<Void> deleteBinary(@PathVariable Long id) {
        log.debug("REST request to delete Binary : {}", id);
        binaryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
