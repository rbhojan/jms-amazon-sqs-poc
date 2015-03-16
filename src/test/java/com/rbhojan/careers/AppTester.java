/**
 * 
 */
package com.rbhojan.careers;

import java.util.Set;

import javax.jms.JMSException;

import com.rbhojan.careers.service.MessageManager;


/**
 * @author rbhojan
 *
 */
public class AppTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MessageManager messageManager = MessageManager.getInstance();
			
			try {
				
				for(int i = 0; i < 10; i++){
					
					messageManager.sendMessage("Message :"+i);
				}
				
				Thread.sleep(3000);
				
				Set<String> messages = messageManager.receiveMessages();
				
				System.out.println("Received Messages : "+messages.toString());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
