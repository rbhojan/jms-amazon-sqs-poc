/**
 * 
 */
package com.rbhojan.careers.domain;


/**
 * @author rbhojan
 * 
 * The Message Domain Object (Immutable). 
 *
 */
public final class Message {

	private final String text;
    
    public Message(String message){
    	
    	this.text=message;
    
    }

	public String getText() {
		return text;
	}
    

	

}
