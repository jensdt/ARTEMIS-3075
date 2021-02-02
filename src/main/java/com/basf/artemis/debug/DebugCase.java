package com.basf.artemis.debug;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class DebugCase {
    public static void main(String[] args) throws Exception {
        // Instantiate connection towards server 2 (61617)
        ConnectionFactory cf0 = new ActiveMQConnectionFactory("tcp://localhost:61617");
        Queue queue = ActiveMQJMSClient.createQueue("exampleQueue");

        for (int i = 0; i < 50; i++){
            createConsumer(cf0, queue);
        }

        // Don't do anything - you should now trigger the failure case by shutting down server 2
        while (true) {
            Thread.sleep(1000);
        }
    }

    private static void createConsumer(ConnectionFactory cf0, Queue queue) throws JMSException {
        Connection connection0 = cf0.createConnection();
        Session session0 = connection0.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        session0.createConsumer(queue);
        connection0.start();
    }

}
