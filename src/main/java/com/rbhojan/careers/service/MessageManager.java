/**
 * 
 */
package com.rbhojan.careers.service;

import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.rbhojan.careers.constants.ConstantsAndAssumptions;
import com.rbhojan.careers.util.AsyncMessageListener;

/**
 * @author rbhojan
 *
 */
public class MessageManager {

	private static Logger logger = Logger.getLogger(MessageManager.class);

	private static volatile MessageManager messageManager;

	private SQSConnectionFactory connectionFactory;

	private Session session;

	private Queue queue;

	private SQSConnection connection;

	private MessageManager() {

		if (logger.isInfoEnabled()) {
			logger.info("Constructing the message manager...");
		}

		this.connectionFactory = SQSConnectionFactory
				.builder()
				.withRegion(Region.getRegion(Regions.US_WEST_1))
				.withAWSCredentialsProvider(
						new PropertiesFileCredentialsProvider(
								ConstantsAndAssumptions.AWS_CREDENTIALS_FILE)).build();

		configure();

	}

	/**
	 * Method to configure and set-up the connection and session
	 * 
	 */
	private void configure() {
		
		if (logger.isInfoEnabled()) {
			logger.info("configure() <--");
		}

		// Create the connection.
		try {
			this.connection = this.connectionFactory.createConnection();

			// Create the non-transacted session with AUTO_ACKNOWLEDGE mode
			this.session = connection.createSession(false,
					Session.CLIENT_ACKNOWLEDGE);

			// Create a queue identity in the session
			AmazonSQSMessagingClientWrapper client = connection
					.getWrappedAmazonSQSClient();

			if (!client.queueExists(ConstantsAndAssumptions.AWS_SQS_QUEUE_FOR_POC)) {
				client.createQueue(ConstantsAndAssumptions.AWS_SQS_QUEUE_FOR_POC);
			}

			this.queue = session.createQueue(ConstantsAndAssumptions.AWS_SQS_QUEUE_FOR_POC);

		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("configure() -->");
		}

	}

	/**
	 * Send Messages to the SQS Queue
	 * 
	 * @param message
	 * @return
	 * @throws JMSException
	 */
	public String sendMessage(String message) throws JMSException {

		if (logger.isInfoEnabled()) {
			logger.info("sendMessage() <--");
		}

		String messageId;

		try {

			configure();

			// Create a producer for the 'TestQueue'.
			MessageProducer producer = session.createProducer(queue);

			// Create the text message.
			TextMessage textMessage = session.createTextMessage(message);

			// Send the message.
			producer.send(textMessage);

			messageId = textMessage.getJMSMessageID();

		} finally {

			releaseResources();
		}

		if (logger.isInfoEnabled()) {
			logger.info("sendMessage() -->");
		}

		return messageId;

	}

	/**
	 * Receive Messages from the SQS Queue
	 * 
	 * @return
	 * @throws JMSException
	 * @throws InterruptedException
	 */
	public Set<String> receiveMessages() throws JMSException,
			InterruptedException {

		if (logger.isInfoEnabled()) {
			logger.info("receiveMessages() <--");
		}

		Set<String> messages = null;

		try {

			configure();

			MessageConsumer consumer = session.createConsumer(queue);

			AsyncMessageListener listener = new AsyncMessageListener();

			// Instantiate and set the message listener for the consumer.
			consumer.setMessageListener(listener);

			// Start receiving incoming messages.
			connection.start();
			
			Thread.sleep(1000);

			messages = listener.receiveMessages();

		} finally {

			releaseResources();
		}

		if (logger.isInfoEnabled()) {
			logger.info("receiveMessages() -->");
		}

		return messages;

	}

	/**
	 * Returns a singleton instance of MessageManager, i.e, 
	 * Returns an existing instance, else creates a new instance 
	 * if no instance exists.
	 * 
	 * @return
	 */
	public static MessageManager getInstance() {

		if (logger.isInfoEnabled()) {
			logger.info("getInstance() <--");
		}

		if (null == messageManager) {

			synchronized (MessageManager.class) {

				if (null == messageManager) {
					messageManager = new MessageManager();
				}

			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("getInstance() -->");
		}

		return messageManager;

	}

	/**
	 * Release all the resources by closing
	 * the SQS Connection.
	 * 
	 */
	public void releaseResources() {
		
		if (logger.isInfoEnabled()) {
			logger.info("releaseResources() <--");
		}

		try {
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("releaseResources() -->");
		}
	}

}
