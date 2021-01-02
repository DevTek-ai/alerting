package com.alerting.eventing;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.alerting.domain.AlertDefinition;
import com.alerting.domain.Change;
import com.alerting.domain.Event;
import com.alerting.domain.QueryResponse;
import com.alerting.repository.AlertDefinitionRepository;
import com.alerting.service.AuthenticateWOA;
import com.alerting.service.FirebaseHandler;
import com.alerting.service.InvokeQuery;
import com.alerting.web.rest.EventResource;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


@Service
public class SQSListener implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(SQSListener.class);

    @Autowired
    AlertDefinitionRepository alertDefinitionRepository;

    public void onMessage(Message message) {
        try {
            // Cast the received message as TextMessage and print the text to screen.
            System.out.println(
                    "===============================================Received: " + ((TextMessage) message).getText());

            String texttMessage = ((TextMessage) message).getText();

            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            Event event = mapper.readValue(texttMessage, Event.class);


            List<AlertDefinition> alertDefinitionList = alertDefinitionRepository.findAll();
            for (AlertDefinition alertDefinition: alertDefinitionList)
            {
                List<String> splitQuery = new ArrayList<>();
                String query = alertDefinition.getAlertRuleQuery();
                if(query!=null){
                     splitQuery= Arrays.asList(query.split("[ .]+"));
                }

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
                    System.out.println("Query matched" + query);
                    log.debug("Query matched"+query);
                    QueryResponse queryResponse = InvokeQuery.getQueryResponse(token,query);

                    log.debug("Invoked Count Service Status = "+queryResponse.getStatus());
                    System.out.println("Invoked Count Service Status = "+queryResponse.getStatus());
                    log.debug("Going to send Notification to following tokens  = "+queryResponse.getFirebaseTokens());

                    if(queryResponse.getStatus()){
                        log.debug("Starting firebase Dispatch");
                        System.out.println("starting firebase disptach");
                        for (String firebaseToken: queryResponse.getFirebaseTokens()) {
                            FirebaseHandler.dispatch(firebaseToken,"default message",1l);
                            log.debug("Firebase message dispatched to"+ firebaseToken);
                        }

                    }
                }else{
                    System.out.println("no Query matched");
                    log.debug("no Query matched");
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // catch (URISyntaxException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
}
