package me.hao0;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.junit.Test;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 21/11/15
 */
public class FailoverTransportTest {

    private String broker = "failover:(tcp://localhost:61616)";

    private String queue = "queue.test.failover";

    private ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(broker);

    private PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(broker);

    private Connection getConn() throws JMSException {
        return connectionFactory.createConnection();
    }

    private Connection getConnFromPool() throws JMSException {
        return pooledConnectionFactory.createConnection();
    }

    @Test
    public void testProducer() throws JMSException, InterruptedException {

        // Create a Connection
        Connection connection = getConnFromPool();
        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue(queue);

        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for (int i=0; i < 10; i++){
            // Create a messages
            TextMessage message = session.createTextMessage("I'm producer, msg id is " + i);
            // Tell the producer to send the message
            producer.send(message);

            Thread.sleep(10000);
        }

        // Clean up
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testConsumer() throws JMSException {
        // Create a Connection
        Connection connection = getConnFromPool();
        connection.start();

        connection.setExceptionListener(new ExceptionListener() {
            @Override
            public void onException(JMSException exception) {
                System.out.println(exception);
            }
        });

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue(queue);

        // Create a MessageConsumer from the Session to the Topic or Queue
        MessageConsumer consumer = session.createConsumer(destination);

        // Wait message
        Message message;
        while ((message =consumer.receive()) != null){
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Received: " + text);
            } else {
                System.out.println("Received: " + message);
            }
        }

        consumer.close();
        session.close();
        connection.close();
    }
}
