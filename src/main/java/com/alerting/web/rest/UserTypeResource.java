package com.alerting.web.rest;

import com.alerting.domain.UserType;
import com.alerting.repository.UserTypeRepository;
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
 * REST controller for managing {@link com.alerting.domain.UserType}.
 */
@RestController
@RequestMapping("/alert/api")
@Transactional
public class UserTypeResource {

    private final Logger log = LoggerFactory.getLogger(UserTypeResource.class);

    private static final String ENTITY_NAME = "alertingUserType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserTypeRepository userTypeRepository;

    public UserTypeResource(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    /**
     * {@code POST  /user-types} : Create a new userType.
     *
     * @param userType the userType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userType, or with status {@code 400 (Bad Request)} if the userType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-types")
    public ResponseEntity<UserType> createUserType(@RequestBody UserType userType) throws URISyntaxException {
        log.debug("REST request to save UserType : {}", userType);
        if (userType.getId() != null) {
            throw new BadRequestAlertException("A new userType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserType result = userTypeRepository.save(userType);
        return ResponseEntity.created(new URI("/api/user-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-types} : Updates an existing userType.
     *
     * @param userType the userType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userType,
     * or with status {@code 400 (Bad Request)} if the userType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-types")
    public ResponseEntity<UserType> updateUserType(@RequestBody UserType userType) throws URISyntaxException {
        log.debug("REST request to update UserType : {}", userType);
        if (userType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserType result = userTypeRepository.save(userType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-types} : get all the userTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userTypes in body.
     */
    @GetMapping("/user-types")
    public List<UserType> getAllUserTypes() {
        log.debug("REST request to get all UserTypes");
        return userTypeRepository.findAll();
    }

    /**
     * {@code GET  /user-types/:id} : get the "id" userType.
     *
     * @param id the id of the userType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-types/{id}")
    public ResponseEntity<UserType> getUserType(@PathVariable Long id) {
        log.debug("REST request to get UserType : {}", id);
        Optional<UserType> userType = userTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userType);
    }

    /**
     * {@code DELETE  /user-types/:id} : delete the "id" userType.
     *
     * @param id the id of the userType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-types/{id}")
    public ResponseEntity<Void> deleteUserType(@PathVariable Long id) {
        log.debug("REST request to delete UserType : {}", id);
        userTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
