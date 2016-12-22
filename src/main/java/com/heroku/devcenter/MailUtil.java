/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heroku.devcenter;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.soap.partner.EmailPriority;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.SendEmailResult;
import com.sforce.soap.partner.SingleEmailMessage;
import com.sforce.ws.ConnectionException;

import db.Util;



public class MailUtil {

	final static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    public static void main(String args[]) {

    }

    public void sendMailAPI(PartnerConnection partnerConnection, String subject, String plainBody, String sender, String recipients, String reportID, String ORG_ID, String USER_ID, String report_path, String created_date) throws FileNotFoundException{

        try {
        	
            SingleEmailMessage message = new SingleEmailMessage();

            message.setBccSender(true);
            message.setEmailPriority(EmailPriority.High);
            message.setSaveAsActivity(false);
            message.setSubject(subject);
            //message.setTargetObjectId(guir.getUserId());
            message.setUseSignature(false);
            message.setHtmlBody(Util.createHTML(reportID));
            message.setPlainTextBody(plainBody);
            message.setToAddresses(new String[]{recipients});
            
            //reads the bcc email from heroku env variables
            String bccEmail = System.getenv("BCC_EMAIL");
            if (null != bccEmail && !bccEmail.isEmpty()) {
                message.setBccAddresses(new String[]{bccEmail});
            }
            message.setBccAddresses(new String[]{"nimoraes@altimetrik.com"});
            SingleEmailMessage[] messages = {message};
            SendEmailResult[] results = partnerConnection.sendEmail(messages);
            if (results[0].isSuccess()) {
                logger.info(" Email Status :" + "The email was sent successfully.");

            } else {
                if(results[0].getErrors()[0].getMessage().contains("bcc compliance is enabled")){
                	logger.info(" The email failed to send " + results[0].getErrors()[0].getMessage());
                	message.setBccSender(false);
                    message.setBccAddresses(null);
                    results = partnerConnection.sendEmail(messages);
                    if (results[0].isSuccess()) {
                        logger.info(" Email Status :" + "The email was sent successfully.");

                    } else {
                        logger.error(" The email failed to send " + results[0].getErrors()[0].getMessage());
                        
                    }
                }else{
                	 logger.error(" The email failed to send " + results[0].getErrors()[0].getMessage());
                }
                
            }

        } catch (ConnectionException ce) {
            ce.printStackTrace();
        }
    }

}
