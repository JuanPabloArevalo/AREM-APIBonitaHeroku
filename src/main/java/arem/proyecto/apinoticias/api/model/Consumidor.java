package arem.proyecto.apinoticias.api.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import javax.jms.Message;

public class Consumidor implements Runnable, ExceptionListener{

    
    public Consumidor(){
        

    }

        
    public void run(){  
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://54.186.68.167:61616?jms.useAsyncSend=true");
            
            // Create a Connection
            Connection connection = connectionFactory.createConnection("smx","smx");
            connection.start();
            
            connection.setExceptionListener(this);
            
            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("events");
            
            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);
            
            // Wait for a message
            Message message = consumer.receive(1000);
            
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Received: " + text);
            } else {
                System.out.println("Received: " + message);
            }
            
            consumer.close();
            session.close();
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onException(JMSException jmse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}