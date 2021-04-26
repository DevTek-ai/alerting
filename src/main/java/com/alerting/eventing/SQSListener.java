package com.alerting.eventing;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Value("${SQS1-DROP-EVENT}")
    private String sqs;

    public void onMessage(Message message) {
        try {


            List<Lookup> lk = SeedLookup();
            // Cast the received message as TextMessage and print the text to screen.
            System.out.println(
                    "===============================================Received: " + ((TextMessage) message).getText());

            String texttMessage = ((TextMessage) message).getText();

            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            Event event = mapper.readValue(texttMessage, Event.class);


            List<AlertDefinition> alertDefinitionList = alertDefinitionRepository.findAll();
            for (AlertDefinition alertDefinition: alertDefinitionList)
            {
                int matchCount = 0;
                List<Change> attributesChangeList = event.getChanges();
                String query = alertDefinition.getAlertRuleQuery();
                List<String> userTypes  =  alertDefinition.getUserTypes();
                String triggeredRule = "";
                if(alertDefinition.getTypeSelection().equalsIgnoreCase(event.getObject())) matchCount++;
//changeOrderAmount
               List<String> splitQuery = new ArrayList<>();
                if(query!=null){
                     splitQuery= Arrays.asList(query.split("[ .]+"));
                }
                for (String word: splitQuery)
                {
//                    if(word.equalsIgnoreCase(event.getObject()))  matchCount++;

                    for (Change changedAttribute: attributesChangeList)
                    {
                        String ov = changedAttribute.getOldValue();
                        String nv =changedAttribute.getNewValue();
                        if(changedAttribute.getAttribute().equals("projectStatusId") || changedAttribute.getAttribute().equalsIgnoreCase("Status") || changedAttribute.getAttribute().equals("projectType") ){
                            ov =  lk.stream().filter(a->a.getId().equals(Integer.parseInt(changedAttribute.getOldValue()))).collect(Collectors.toList()).get(0).getValue();
                            nv = lk.stream().filter(a->a.getId().equals(Integer.parseInt(changedAttribute.getNewValue()))).collect(Collectors.toList()).get(0).getValue();

                        }

                        if(word.equalsIgnoreCase(changedAttribute.getAttribute())) {
                            matchCount++;
                            System.out.println("second count" + matchCount);
                            log.debug("second count"+matchCount);
                            if(alertDefinition.getBehaviourSelection().equals("Change")) {
                                String oldValue =  changedAttribute.getOldValue();
                                if(oldValue==null && changedAttribute.getNewValue()!=null ) { matchCount++;
                               triggeredRule = alertDefinition.getTypeSelection() +" "+ alertDefinition.getAttributeSelection() + " Changed to " + changedAttribute.getNewValue();
                                }
                                else if(oldValue!=null && !changedAttribute.getOldValue().equals(changedAttribute.getNewValue())) {
                                    matchCount++;
                                    if(changedAttribute.getAttribute().equals("projectStatusId")){
                                        triggeredRule = alertDefinition.getTypeSelection()+" Status Changed from " + ov + " to "+ nv;
                                    }
                                    else
                                    triggeredRule = alertDefinition.getTypeSelection()+" "+ alertDefinition.getAttributeSelection() + " Changed from " + ov + " to "+ nv;
                                }
                            }
                            if (alertDefinition.getCustomAttributeSelection()!=null &&
                                    alertDefinition.getCustomAttributeSelection().equalsIgnoreCase(changedAttribute.getNewValue()))
                            {
                                matchCount++;
                                if(changedAttribute.getAttribute().equals("projectStatusId")){
                                    triggeredRule = alertDefinition.getTypeSelection()+" Status Changed from " + ov + " to "+ nv;
                                }
                                else
                                triggeredRule = alertDefinition.getTypeSelection() +" "+ alertDefinition.getAttributeSelection() + " Changed from " + ov + "to "+ nv;

                            }
                            if(alertDefinition.getAlertRuleQuery().contains("BETWEEN"))
                            {
                                matchCount++;
                                break;
                            }
                        }
                     }//attribute matching


                }//split Query
//                for (Change changedAttribute: attributesChangeList)
//                {
//                    //matching attribute
//                    if(alertDefinition.getAttributeSelection().equals(changedAttribute.getAttribute()))  matchCount++;
//                    //matching change
//
//                }
//
//                List<String> splitQuery = new ArrayList<>();
//                String query = alertDefinition.getAlertRuleQuery();
//                if(query!=null){
//                     splitQuery= Arrays.asList(query.split("[ .]+"));
//                }

//                int matchCount = 0;
//                List<Change> changeList = event.getChanges();
//                for (String word: splitQuery)
//                {
//                    if(word.equalsIgnoreCase(event.getObject()))  matchCount++;
//
//                    for (Change changeAttribute: changeList)
//                    {
//                        if(word.equalsIgnoreCase(changeAttribute.getAttribute()))  matchCount++;
//                     }//attribute matching
//
//
//                }//split Query
//                for (Change changeAttribute: changeList) {
//                    if(alertDefinition.getBehaviourSelection().equals("Change")) {
//                        if(!changeAttribute.getOldValue().equals(changeAttribute.getNewValue())) matchCount++;
//                    }
//                    if (alertDefinition.getCustomAttributeSelection()!=null &&
//                        alertDefinition.getCustomAttributeSelection().equalsIgnoreCase(changeAttribute.getNewValue()))
//                    {
//                        matchCount++;
//                    }
//                    if(alertDefinition.getAlertRuleQuery().contains("BETWEEN"))
//                    {
//                        matchCount++;
//                        break;
//                    }
//                }
                System.out.println("match count" + matchCount);
                log.debug("match count"+matchCount);
                if(matchCount == 3)
                {
                    String token = new AuthenticateWOA().getAccessToken();
                    System.out.println("Query matched" + query);
                    log.debug("Query matched"+query);
                    AlertQuery aQuery = new AlertQuery();
                    if(alertDefinition.getBehaviourSelection().equalsIgnoreCase("Change")){
                        aQuery.setRunQuery(false);
                    }
                    aQuery.setUsers(alertDefinition.getUserTypes());
                    aQuery.setQueryString(query);
                    aQuery.setParam(alertDefinition.getCustomAttributeSelection());
                    aQuery.setFrom(alertDefinition.getFrom());
                    aQuery.setTo(alertDefinition.getTo());
                    QueryResponse queryResponse = InvokeQuery.getQueryResponse(token,aQuery);

                    log.debug("Invoked Count Service Status = "+queryResponse.getStatus());
                    System.out.println("Invoked Count Service Status = "+queryResponse.getStatus());


                    if(queryResponse.getStatus()){
                       String result  = queryResponse.getData();
                        log.debug("data received = "+result);
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
                               String category = alertDefinition.getCategory().name();
                               if(category.equals("INFO")){
                                   history.setCategory(1);
                               }
                               if(category.equals("WARNING")){
                                   history.setCategory(2);
                               }
                               if(category.equals("ERROR")){
                                   history.setCategory(3);
                               }

                               history.setSubject(event.getName());
                               history.setAttribute(alertDefinition.getAttributeSelection());
                               history.setBehaviour(alertDefinition.getBehaviourSelection());
                               history.setMessage(triggeredRule);
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
                           thirdPartyDispatchForEmail.setMessage(triggeredRule);
                           thirdPartyDispatchForEmail.setSubject(alertDefinition.getTitle());
                           thirdPartyDispatchForEmail.setTo(alertDefinition.getRecipientEmailAddress());
                           awsEmail.sendSQS(thirdPartyDispatchForEmail);

                           AWSService awsSms  = new AWSService(sqs);
                           ThirdPartyDispatch thirdPartyDispatchForSMS = new ThirdPartyDispatch();
                           List<String> sms = new ArrayList<String>();
                           sms.add("sms");
                           thirdPartyDispatchForSMS.setChannels(sms);
                           thirdPartyDispatchForSMS.setMessage(triggeredRule);
                           thirdPartyDispatchForSMS.setSubject(alertDefinition.getTitle());
                           thirdPartyDispatchForSMS.setTo((alertDefinition.getRecipientPhoneNumber()));
                           awsSms.sendSQS(thirdPartyDispatchForSMS);
                       }


                    }
                }else{
                    System.out.println("no Query matched"+query);
                    log.debug("no Query matched"+query);
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

    private List<Lookup> SeedLookup() {
        List<Lookup> lk = new ArrayList<>();
        lk.add(new Lookup(1,"Admin"));
        lk.add(new Lookup(2,"Accounting"));
        lk.add(new Lookup(3,"Client"));
        lk.add(new Lookup(4,"General"));
        lk.add(new Lookup(5,"Project"));
        lk.add(new Lookup(6,"Vendor"));
        lk.add(new Lookup(7,"NEW"));
        lk.add(new Lookup(8,"ACTIVE"));
        lk.add(new Lookup(9,"PUNCH"));
        lk.add(new Lookup(  10,"CLOSED"));
        lk.add(new Lookup(11,"INVOICED"));
        lk.add(new Lookup(12,"Active"));
        lk.add(new Lookup(13,"InActive"));
        lk.add(new Lookup(14,"Do Not Use"));
        lk.add(new Lookup(15,"Expired"));
        lk.add(new Lookup(16,"Work Order"));
        lk.add(new Lookup(17,"Change Order"));
        lk.add(new Lookup(27,"Vendor Work Order"));
        lk.add(new Lookup(  28,"Original SOW"));
        lk.add(new Lookup(29,"Change Order"));
        lk.add(new Lookup(30,"Draw"));
        lk.add(new Lookup(31,"Material"));
        lk.add(new Lookup(32,"Adjustment"));
        lk.add(new Lookup(33,"CANCELLED"));
        lk.add(new Lookup(  34,"Active"));
        lk.add(new Lookup(35,"Cancelled"));
        lk.add(new Lookup(36,"Completed"));

        lk.add(new Lookup(37,"Paid"));
        lk.add(new Lookup(38,"Operational"));
        lk.add(new Lookup(39,"Original SOW"));
        lk.add(new Lookup(40,"Signed Agreement"));
        lk.add(new Lookup(41,"PAID"));
        lk.add(new Lookup(42,"Project Invoice"));
        lk.add(new Lookup(43,"OTHER"));
        lk.add(new Lookup(44,"TURNS"));
        lk.add(new Lookup(45,"OCCUPIED"));
        lk.add(new Lookup(46,"RENOS"));
        lk.add(new Lookup(47,"ROOFS"));
        lk.add(new Lookup(48,"Vendor Invoice"));
        lk.add(new Lookup(49,"Paid"));
        lk.add(new Lookup(62,"WO Paid"));
        lk.add(new Lookup(63,"Inspections"));
        lk.add(new Lookup(64,"Commercial"));
        lk.add(new Lookup(65,"Estimates"));
        lk.add(new Lookup(66,"New"));
        lk.add(new Lookup(67,"Work In Progress"));
        lk.add(new Lookup(68,"Paid"));
        lk.add(new Lookup(69,"Resolved"));
        lk.add(new Lookup(70,"Rejected"));

        return lk;
    }
}
