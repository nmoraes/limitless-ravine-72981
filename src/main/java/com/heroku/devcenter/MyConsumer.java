package com.heroku.devcenter;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import db.DatabaseRabbit;

public class MyConsumer extends DefaultConsumer {

	public MyConsumer(Channel channel) {
		super(channel);
	}

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
        throws IOException {
  	  
  	  
  	  byte[] valueDecoded= Base64.decodeBase64(body);
  	 
  	  String message = new String(valueDecoded, "UTF-8");

  	 	    		           
      System.out.println(" [x] Received '" + message + "'");
      
      
      DatabaseRabbit db = DatabaseRabbit.getInstance();
      try {
		db.insert(message);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
      
         
    }
    	
}
