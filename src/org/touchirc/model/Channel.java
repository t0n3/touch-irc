package org.touchirc.model;

import java.util.HashMap;

public class Channel extends Conversation {
	
	private String topic;
	private HashMap<Character,Boolean> modes;
	
	public Channel(String name){
		super(name);
		this.topic = "";
		this.modes = new HashMap<Character, Boolean>();
	}
	
	/**
	 * Set the topic of the channel
	 * 
	 * @param newTopic : String
	 */
	public void setTopic(String newTopic){
		this.topic = newTopic;
	}
	
	/**
	 * Return the topic of the channel
	 * 
	 * @return String : the topic of the channel
	 */
	public String getTopic(){
		return this.topic;
	}
	
	/**
	 * Change a channel mode.
	 * True to activate the mode, false to deactivate it
	 * 
	 * @param mode : Character
	 * @param active : boolean
	 */
	public void changeMode(Character mode, boolean active){
		this.modes.put(mode,active);
	}
	
	/**
	 * Return the list of channel modes.
	 * 
	 * @return The list of channel modes 
	 */
	public HashMap<Character, Boolean> getModes(){
		return this.modes;
	}
}
