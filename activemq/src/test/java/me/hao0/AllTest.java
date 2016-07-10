package me.hao0;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import javax.jms.Connection;
import javax.jms.JMSException;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 21/11/15
 */
public class AllTest {

    @Test
    public void testConnect() throws JMSException, InterruptedException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        Connection connection = connectionFactory.createConnection();

        connection.start();

        Thread.sleep(10000);

        connection.close();
    }
}
