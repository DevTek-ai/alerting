package com.alerting.web.rest;

import com.alerting.domain.Schedular;
import com.alerting.repository.SchedularRepository;
import com.alerting.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.alerting.domain.Schedular}.
 */
@RestController
@RequestMapping("/alert/api")
@Transactional
public class SchedularResource {

    private final Logger log = LoggerFactory.getLogger(SchedularResource.class);

    private static final String ENTITY_NAME = "alertingSchedular";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchedularRepository schedularRepository;

    public SchedularResource(SchedularRepository schedularRepository) {
        this.schedularRepository = schedularRepository;
    }

    /**
     * {@code POST  /schedulars} : Create a new schedular.
     *
     * @param schedular the schedular to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schedular, or with status {@code 400 (Bad Request)} if the schedular has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/schedulars")
    public ResponseEntity<Schedular> createSchedular(@Valid @RequestBody Schedular schedular) throws URISyntaxException {
        log.debug("REST request to save Schedular : {}", schedular);
        if (schedular.getId() != null) {
            throw new BadRequestAlertException("A new schedular cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Schedular result = schedularRepository.save(schedular);
        return ResponseEntity.created(new URI("/api/schedulars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /schedulars} : Updates an existing schedular.
     *
     * @param schedular the schedular to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schedular,
     * or with status {@code 400 (Bad Request)} if the schedular is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schedular couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/schedulars")
    public ResponseEntity<Schedular> updateSchedular(@Valid @RequestBody Schedular schedular) throws URISyntaxException {
        log.debug("REST request to update Schedular : {}", schedular);
        if (schedular.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Schedular result = schedularRepository.save(schedular);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, schedular.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /schedulars} : get all the schedulars.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schedulars in body.
     */
    @GetMapping("/schedulars")
    public List<Schedular> getAllSchedulars(@RequestParam(required = false) String filter) {
        if ("alertdefinition-is-null".equals(filter)) {
            log.debug("REST request to get all Schedulars where alertDefinition is null");
            return StreamSupport
                .stream(schedularRepository.findAll().spliterator(), false)
                .filter(schedular -> schedular.getAlertDefinition() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Schedulars");
        return schedularRepository.findAll();
    }

    /**
     * {@code GET  /schedulars/:id} : get the "id" schedular.
     *
     * @param id the id of the schedular to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schedular, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/schedulars/{id}")
    public ResponseEntity<Schedular> getSchedular(@PathVariable Long id) {
        log.debug("REST request to get Schedular : {}", id);
        Optional<Schedular> schedular = schedularRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(schedular);
    }

    /**
     * {@code DELETE  /schedulars/:id} : delete the "id" schedular.
     *
     * @param id the id of the schedular to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/schedulars/{id}")
    public ResponseEntity<Void> deleteSchedular(@PathVariable Long id) {
        log.debug("REST request to delete Schedular : {}", id);
        schedularRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
