/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.heroku.devcenter;

import com.sforce.soap.partner.EmailFileAttachment;
import com.sforce.soap.partner.EmailPriority;
import com.sforce.soap.partner.SendEmailResult;
import com.sforce.soap.partner.SingleEmailMessage;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MailUtil {

	final static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    public static void main(String args[]) {

    }

    public void sendMailAPI(PartnerConnection partnerConnection, String subject, String body, String plainBody, String sender, String recipients, File filename, String ORG_ID, String USER_ID, String report_path, String created_date) throws FileNotFoundException{

 	   System.out.println("send Mail API");

    	
        try {
        	
            SingleEmailMessage message = new SingleEmailMessage();
            if (null != filename && filename.length() != 0) {

                EmailFileAttachment efa = new EmailFileAttachment();
                FileInputStream fis = new FileInputStream(filename.getAbsolutePath().toString());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                try {
                    for (int readNum; (readNum = fis.read(buf)) != -1;) {
                        bos.write(buf, 0, readNum);
                    }
                } catch (IOException ex) {
                    logger.error(" Failed to attach the report" + ex.getMessage());
                }
                byte[] fileBody = bos.toByteArray();
                efa.setBody(fileBody);
                efa.setContentType("application/pdf");
                efa.setFileName("LightningExperienceReadinessReport.pdf");
                EmailFileAttachment[] efas = {efa};
                message.setFileAttachments(efas);
            }
            message.setBccSender(true);
            message.setEmailPriority(EmailPriority.High);
            message.setSaveAsActivity(false);
            message.setSubject(subject);
            //message.setTargetObjectId(guir.getUserId());
            message.setUseSignature(false);
            message.setHtmlBody(body);
            message.setPlainTextBody(plainBody);
            message.setToAddresses(new String[]{recipients});
            
            //reads the bcc email from heroku env variables
            String bccEmail = System.getenv("nico.moraes@gmail.com");
            if (null != bccEmail && !bccEmail.isEmpty()) {
                message.setBccAddresses(new String[]{bccEmail});
            }
            
            message.setBccAddresses(new String[]{"nimoraes@altimetrik.com"});
            SingleEmailMessage[] messages = {message};
            SendEmailResult[] results = partnerConnection.sendEmail(messages);
            if (results[0].isSuccess()) {
                logger.info(" Email Status :" + "The email was sent successfully.");
         	   System.out.println("Email Status : The email was sent successfully");

            } else {
                if(results[0].getErrors()[0].getMessage().contains("bcc compliance is enabled")){
                	logger.info(" The email failed to send " + results[0].getErrors()[0].getMessage());
                	message.setBccSender(false);
                    message.setBccAddresses(null);
                    results = partnerConnection.sendEmail(messages);
                    if (results[0].isSuccess()) {
                        logger.info(" Email Status :" + "The email was sent successfully.");
                  	   System.out.println("Email Status : The email was sent successfully");


                    } else {
                        logger.error(" The email failed to send " + results[0].getErrors()[0].getMessage());
                  	   System.out.println("The email failed to send");

                        
                    }
                }else{
                	 logger.error(" The email failed to send ");
                	  System.out.println("The email failed to send");

                }
                
            }

            if (null != filename && filename.length() != 0) {
                try {
                    deleteDir(filename);
                    File zipFile = new File(report_path + "retrieveResults_" + ORG_ID + "_" + USER_ID + "_" + created_date + ".zip");
                    File unzipFile = new File(report_path + "retrieveResults_" + ORG_ID + "_" + USER_ID + "_" + created_date);
                    deleteDir(zipFile);
                    deleteDir(unzipFile);
                    logger.info(" Report deleted from the system");
                } catch (Exception e) {
                    logger.error(" Sorry unable to delete report" + e.getMessage());
                }
            }
        } catch (ConnectionException ce) {
            ce.printStackTrace();
        }
    }

    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        System.out.println("The directory is deleted.");
        return dir.delete();

    }
}
