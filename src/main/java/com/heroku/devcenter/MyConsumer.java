package com.heroku.devcenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;


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
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
      
      
//      JSONObject jsonObj = new JSONObject(message);      
//      String token = (String) jsonObj.get("token");
//      String instance_url = (String) jsonObj.get("instance_url");
//      String current_version = (String) jsonObj.get("current_version");
//      
//      String email_id = (String) jsonObj.get("email_id");
//      String org_id = (String) jsonObj.get("org_id");
//      String user_id = (String) jsonObj.get("user_id");
//      
//      String created_date = (String) jsonObj.get("created_date");
//      String content_version_uploader = (String) jsonObj.get("content_version_uploader");
//      String reportIdDb = (String) jsonObj.get("reportIdDb");
//      
           
      
//      try {
//		
//      	PartnerConnection partnerConection = DataModel.createPartnerConection(token, instance_url, current_version);
//      	
//      	MyConsumer.sendEmail(partnerConection, email_id, org_id, user_id, created_date);
//		
//      } catch (Exception e) {
//			
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
      
      
    }
    
    
    public static void sendEmail(PartnerConnection partnerConection,String email, String org_id, String user_id, String created_date) throws FileNotFoundException {
		  
//		  MailUtil mailUtil = new MailUtil();
//		  
//		  mailUtil.sendMailAPI(partnerConection,"PRUEBA RABBIT", email, "BIEN HECHO RABBIT", "", null, null, org_id, user_id, null, created_date);
//		  
//		  
		  
	  }
    
    
	
}
