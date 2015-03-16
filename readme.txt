*******************************************************************************************************************
Author : 
	@rbhojan

Comments : 
	This file contains the basic guidelines regarding the usage/inspection of this POC for 
	sending/receiving messages using Amazon SQS and JMS.
	
Feedback : 
	I would be happy to receive feedback. 
	Please send feedback to rbhojan.social@gmail.com
	LinkedIn : www.linkedin.com/in/rbhojan
	Twitter : @rbhojan
*******************************************************************************************************************

Note:
	1. All the Constants and Assumptions are defined as constants in ConstantsAndAssumptions.java.
	2. The Core logic to Send/Receive messages is written in MessageManager.java (com.rbhojan.careers.service).
	
*Steps to run the POC as a stand-alone project :

	1. Run the main method of the class named AppTester.java (in the test package - com.rbhojan.careers)

*Steps to run the POC as a RESTful Web-Service

	1. Run the class AppLauncher.java (in the main package - com.rbhojan.careers.controller) as a SpringBoot App. 
	   This will run the project in the inbuilt Tomcat server
	2. To Send Messages :
	   a. Hit the following URL in a browser : http://localhost:8080/message/send/ to send default messages
	   
	   b. Alternatively you can provide the query parameters in the url as shown in the example below :
			e.g. 
			http://localhost:8080/message/send/?message=hello
			
	   c. The response is the Message Id of the message, for future reference.
		
	3. To Receive Messages :
	   a. Hit the following URL in a browser : http://localhost:8080/message/receive/ to send default messages
	   
	   b. The response is a collection of messages, sent in the json format.
	   
================================================ END =============================================================
	