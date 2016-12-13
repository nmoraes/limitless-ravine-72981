package com.heroku.devcenter;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.QueueingConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ReciveFormQueue {

    final static Logger logger = LoggerFactory.getLogger(ReciveFormQueue.class);

	 private final static String QUEUE_NAME = "mailmkt";

    
    
    public static void main(String[] args) throws Exception {

    	   ConnectionFactory factory = new ConnectionFactory();
   	    factory.setHost("localhost");
   	    Connection connection = factory.newConnection();
   	    Channel channel = connection.createChannel();

   	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
   	 
   	    System.out.println(" [*] Waiting for messages from Lightning Readiness");
   	    	  
   	    Consumer consumer = new MyConsumer(channel); 
   	    
   	    channel.basicConsume(QUEUE_NAME, true, consumer);
    }

}
