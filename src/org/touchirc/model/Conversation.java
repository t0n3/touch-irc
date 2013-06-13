package org.touchirc.model;

import java.util.LinkedList;

public class Conversation {

	private LinkedList<Message> messagesBuffer;
	private LinkedList<Message> messagesHistory;
	private String title;
	
	public Conversation(String title){
		this.messagesBuffer = new LinkedList<Message>();
		this.messagesHistory = new LinkedList<Message>();
		this.title = title;
	}
	
	public void addMessage(Message m){
		this.messagesBuffer.add(m);
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public LinkedList<Message> getBuffer(){
		return this.messagesBuffer;
	}
	
	public void cleanBuffer(){
		this.messagesHistory.addAll(this.messagesBuffer);
		this.messagesBuffer.clear();
	}
	
	public LinkedList<Message> getHistory(){
		return this.messagesHistory;
	}
	
	
}
