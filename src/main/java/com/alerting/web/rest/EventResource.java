package com.alerting.web.rest;

import com.alerting.domain.Event;
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
@RequestMapping("/api")
@Transactional
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);

    private static final String ENTITY_NAME = "alertingOperator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperatorRepository operatorRepository;

    public EventResource(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    /**
     * {@code POST  /operators} : Create a new operator.
     *
     * @param operator the operator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operator, or with status {@code 400 (Bad Request)} if the operator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/events")
    public void createOperator(@RequestBody Event event) throws URISyntaxException {
        log.debug("----new event received--- : {}", event);


    }

}
