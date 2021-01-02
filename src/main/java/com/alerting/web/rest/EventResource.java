package com.alerting.web.rest;

import com.alerting.domain.*;
import com.alerting.repository.AlertDefinitionRepository;
import com.alerting.repository.AlertHistoryRepository;
import com.alerting.repository.OperatorRepository;

import com.alerting.service.AuthenticateWOA;
import com.alerting.service.FirebaseHandler;
import com.alerting.service.InvokeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

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

    @Value("${woa-url}")
    private String hostUrl;

    private final OperatorRepository operatorRepository;
    private final AlertDefinitionRepository alertDefinitionRepository;
    private final AlertHistoryRepository alertHistoryRepository;

    public EventResource(OperatorRepository operatorRepository, AlertDefinitionRepository alertDefinitionRepository, AlertHistoryRepository alertHistoryRepository) {
        this.operatorRepository = operatorRepository;
        this.alertDefinitionRepository = alertDefinitionRepository;
        this.alertHistoryRepository = alertHistoryRepository;
    }

    /**
     * {@code POST  /operators} : Create a new operator.
     *
     * @param @operator the operator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operator, or with status {@code 400 (Bad Request)} if the operator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/events")
    public void createOperator(@RequestBody Event event) throws URISyntaxException {
        log.debug("----new event received--- : {}", event);

        List<AlertDefinition> alertDefinitionList = alertDefinitionRepository.findAll();
        for (AlertDefinition alertDefinition: alertDefinitionList)
        {
           String query = alertDefinition.getAlertRuleQuery();
           List<String> splitQuery= Arrays.asList(query.split("[ .]+"));
           int matchCount = 0;
               for (String word: splitQuery)
                {
                    if(word.equalsIgnoreCase(event.getObject()))  matchCount++;
                    List<Change> changeList = event.getChanges();
                    for (Change changeAttribute: changeList)
                    {
                        if(word.equalsIgnoreCase(changeAttribute.getAttribute()))  matchCount++;
                        if(word.equalsIgnoreCase(changeAttribute.getNewValue()))  matchCount++;
                    }//attribute matching


                }//split Query

            if(matchCount == 3)
            {
                String token = new AuthenticateWOA().getAccessToken();
                log.debug("Query matched"+query);
                QueryResponse queryResponse = InvokeQuery.getQueryResponse(token,query);

                log.debug("Invoked Count Service Status = "+queryResponse.getStatus());
                log.debug("Going to send Notification to following tokens  = "+queryResponse.getFirebaseTokens());

                if(queryResponse.getStatus()){
                    log.debug("Starting firebase Dispatch");
                    for (String firebaseToken: queryResponse.getFirebaseTokens()) {

                        log.debug("Firebase message dispatched to"+ firebaseToken);
                        AlertHistory history = new AlertHistory();
                        history.setDateCreated(Instant.now());
                        history.setWebSockectRead(false);
                        history.setCategory(1);
                        history.setMessage("default message");
                        history.setSubject("test");
                        AlertHistory save = alertHistoryRepository.save(history);
                        FirebaseHandler.dispatch(firebaseToken,"default message",save.getId());
                    }

                }
            }else{
                log.debug("no Query matched");
            }

        }//alertDefinitions

    }//method ends




}
