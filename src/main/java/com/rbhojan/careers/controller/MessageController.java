package com.rbhojan.careers.controller;

import java.util.Set;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rbhojan.careers.constants.ConstantsAndAssumptions;
import com.rbhojan.careers.service.MessageManager;

@RestController
public class MessageController {

    Logger logger = Logger.getLogger(MessageController.class);
    
    /**
     * Method to send Messages to SQS
     * 
     * @param inputMessage
     * @return
     */
	@RequestMapping("/message/send/")
    public String sendMessage(@RequestParam(value="message",required=false) String inputMessage) {
    	
		if (logger.isInfoEnabled()) {
			logger.info("sendMessage() <--");
		}
		
		if(null == inputMessage || inputMessage.isEmpty()){
			inputMessage = ConstantsAndAssumptions.DEFAULT_MESSAGE;
    		
    	}
    	
    	String messageId=null;
    	
    	MessageManager messageManager = MessageManager.getInstance();
    	
    	try {
    		messageId = messageManager.sendMessage(inputMessage);
			
		} catch (JMSException e) {
			e.printStackTrace();
			messageId = "Exception Processing the Request! Try again later...";
		}
    	
    	if (logger.isInfoEnabled()) {
			logger.info("sendMessage() -->");
		}
    	
    	return messageId;
    }
	
	/**
	 * Method to receive messages from SQS
	 * 
	 * @return
	 */
	@RequestMapping("/message/receive/")
    public Set<String> receiveMessages() {
    	
		if (logger.isInfoEnabled()) {
			logger.info("receiveMessages() <--");
		}
		
		Set<String> messages = null;
		
		MessageManager messageManager = MessageManager.getInstance();
    	
    	try {
    		messages = messageManager.receiveMessages();
			
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	if (logger.isInfoEnabled()) {
			logger.info("receiveMessages() -->");
		}
    	
    	return messages;
    }

}
