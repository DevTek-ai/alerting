package com.alerting.eventing;

import com.alerting.domain.ThirdPartyDispatch;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

public class AWSService {

    private final Logger log = LoggerFactory.getLogger(AWSService.class);
    private final AmazonSQS SQS = AmazonSQSClientBuilder.defaultClient();

    String sqsQueue;

    public AWSService(String queue) {
        this.sqsQueue = queue;
    }

    @Async
    public void sendSQS(ThirdPartyDispatch event)  {

        ObjectMapper mapper = new ObjectMapper();
        try{
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
            log.debug("--=======SQS==" +sqsQueue);
            log.debug("--========================================================-event=" +json);
            SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(sqsQueue)
                .withMessageBody(json)
                .withMessageGroupId("Group1");
            SQS.sendMessage(send_msg_request);
        }
        catch(Exception ex){
            ex.printStackTrace();;
        }
    }
}
