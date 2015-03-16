/**
 * 
 */
package com.rbhojan.careers.util;

import java.util.Set;
import java.util.TreeSet;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

/**
 * @author rbhojan
 *
 */
public class AsyncMessageListener implements MessageListener{
	
	private static Logger logger = Logger.getLogger(AsyncMessageListener.class);
	
	private final Set<String> messages;
	
	public AsyncMessageListener(){
		messages = new TreeSet<String>();
	}
	
	public Set<String> receiveMessages(){
		
		return messages;
		
	}
	

	@Override
	public void onMessage(Message message) {
		
		if (logger.isInfoEnabled()) {
			logger.info("onMessage() <--");
		}
		
		try {
            
			if (message != null) {
            	
            	String text = ((TextMessage) message).getText();
            	messages.add(text);
            	
            	if(logger.isInfoEnabled()){
            		logger.info("Received: " +text);
            	}
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
		
		if (logger.isInfoEnabled()) {
			logger.info("onMessage() -->");
		}
		
	}

}
