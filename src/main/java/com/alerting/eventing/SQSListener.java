package com.alerting.eventing;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.alerting.domain.*;
import com.alerting.repository.AlertDefinitionRepository;
import com.alerting.repository.AlertHistoryRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


@Service
public class SQSListener implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(SQSListener.class);

    @Autowired
    AlertDefinitionRepository alertDefinitionRepository;
    @Autowired
     AlertHistoryRepository alertHistoryRepository;

    @Value("${sqs1-drop-event}")
    private String sqs;

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
                List<Change> changeList = event.getChanges();
                for (String word: splitQuery)
                {
                    if(word.equalsIgnoreCase(event.getObject()))  matchCount++;

                    for (Change changeAttribute: changeList)
                    {
                        if(word.equalsIgnoreCase(changeAttribute.getAttribute()))  matchCount++;
                     }//attribute matching


                }//split Query
                for (Change changeAttribute: changeList) {
                    if (alertDefinition.getCustomAttributeSelection()!=null &&
                        alertDefinition.getCustomAttributeSelection().equalsIgnoreCase(changeAttribute.getNewValue()))
                    {
                        matchCount++;
                    }
                    if(alertDefinition.getAlertRuleQuery().contains("BETWEEN"))
                    {
                        matchCount++;
                        break;
                    }
                }
                if(matchCount == 3)
                {
                    String token = new AuthenticateWOA().getAccessToken();
                    System.out.println("Query matched" + query);
                    log.debug("Query matched"+query);
                    AlertQuery aQuery = new AlertQuery();
                    aQuery.setQueryString(query);
                    aQuery.setParam(alertDefinition.getCustomAttributeSelection());
                    aQuery.setFrom(alertDefinition.getFrom());
                    aQuery.setTo(alertDefinition.getTo());
                    QueryResponse queryResponse = InvokeQuery.getQueryResponse(token,aQuery);

                    log.debug("Invoked Count Service Status = "+queryResponse.getStatus());
                    System.out.println("Invoked Count Service Status = "+queryResponse.getStatus());


                    if(queryResponse.getStatus()){
                       String result  = queryResponse.getData();
                       Integer count = Integer.parseInt(result);
                       if(count>0)
                       {
                           log.debug("Starting firebase Dispatch");
                           System.out.println("starting firebase disptach");
                           List<UserForAlert> userForAlerts = queryResponse.getUserForAlerts();
                           for (UserForAlert user : userForAlerts ) {
                               AlertHistory history = new AlertHistory();
                               history.setTriggeredId(Long.valueOf(event.getId()));
                               history.setTriggeredType(event.getObject());
                               history.setDateCreated(Instant.now());
                               history.setWebSockectRead(false);
                               history.setCategory(1);
                               history.setAttribute(alertDefinition.getAttributeSelection());
                               history.setBehaviour(alertDefinition.getBehaviourSelection());
                               history.setMessage(alertDefinition.getMessage());
                               history.setSubject(alertDefinition.getTitle());
                               history.setLogin(user.getLogin());
                               AlertHistory save = alertHistoryRepository.save(history);

                               FirebaseHandler.dispatch(user.getFirebaseToken(),"default message",save.getId());
                               System.out.println("message dispatched");
                           }
                           AWSService awsEmail  = new AWSService(sqs);
                           ThirdPartyDispatch thirdPartyDispatchForEmail = new ThirdPartyDispatch();
                           List<String> emails = new ArrayList<String>();
                           emails.add("email");
                           thirdPartyDispatchForEmail.setChannels(emails);
                           thirdPartyDispatchForEmail.setMessage(alertDefinition.getMessage());
                           thirdPartyDispatchForEmail.setSubject(alertDefinition.getTitle());
                           thirdPartyDispatchForEmail.setTo(alertDefinition.getRecipientEmailAddress());
                           awsEmail.sendSQS(thirdPartyDispatchForEmail);

                           AWSService awsSms  = new AWSService(sqs);
                           ThirdPartyDispatch thirdPartyDispatchForSMS = new ThirdPartyDispatch();
                           List<String> sms = new ArrayList<String>();
                           sms.add("sms");
                           thirdPartyDispatchForSMS.setChannels(sms);
                           thirdPartyDispatchForSMS.setMessage(alertDefinition.getMessage());
                           thirdPartyDispatchForSMS.setSubject(alertDefinition.getTitle());
                           thirdPartyDispatchForSMS.setTo((alertDefinition.getRecipientPhoneNumber()));
                           awsSms.sendSQS(thirdPartyDispatchForSMS);
                       }


                    }
                }else{
                    System.out.println("no Query matched");
                    log.debug("no Query matched");
                }
            }

        }
        catch (JMSException e) {
            System.out.println("JMS EXCEPTION");
        } catch (JsonParseException e) {
            System.out.println("JSON PARSE EXCEPTION");
        } catch (JsonMappingException e) {
            System.out.println("JSON MAPPING EXCEPTION");
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        } catch (URISyntaxException e) {
            System.out.println("URI EXCEPTION");
        } catch(Exception e){
            System.out.println(" EXCEPTION");
        }
    }
}
