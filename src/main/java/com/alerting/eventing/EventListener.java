package com.alerting.eventing;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

@Component
public class EventListener {

    private final SQSListener sqsListener;

    public EventListener(SQSListener sqsListener){

        this.sqsListener = sqsListener;
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
            new ProviderConfiguration(),
            AmazonSQSClientBuilder.defaultClient()
        );

        // Create the connection.
        SQSConnection connection;
        try {
            connection = connectionFactory.createConnection();

            // Create the non-transacted session with CLIENT_ACKNOWLEDGE mode.
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a queue identity and specify the queue name to the session
            Queue queue = session.createQueue("event_processing_queue.fifo");

            // Create a consumer for the 'MyQueue'.
            MessageConsumer consumer = session.createConsumer(queue);

            // Instantiate and set the message listener for the consumer.
            consumer.setMessageListener(sqsListener);

            // Start receiving incoming messages.
            connection.start();

            // Wait for 1 second. The listener onMessage() method is invoked when a message is received.
            Thread.sleep(1000);
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void listen(){

    }

}
