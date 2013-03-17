package org.touchirc.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class Server extends PircBot {
	
	private String name;
	private String host;
	private int port;
	private String charset = "UTF-8";	
	private String password = "";
	
	// private boolean skipMOTD = true;
	
	// TODO Add SSL support
	private boolean useSSL = false;
	
	public Server(String name, String host, int port){
		this.name = name;
		this.host = host;
		this.port = port;
		try {
			this.setEncoding(this.charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setLogin("touchirc");
		this.setVersion("Touch IRC - IRC Client for Android");
		this.setFinger("You ought to be arrested for fingering a droïd !");
	}
	
	public Server(String name, String host, int port, String password){
		this(name, host, port);
		this.password = password;
	}
	
	public Server(String name, String host, int port, String password, String charset){
		this(name, host, port, password);
		try {
			this.setEncoding(charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Connect to the IRC Server
	 */
	public void connect() throws NickAlreadyInUseException, IOException, IrcException{
		this.startIdentServer();
		if(this.password.isEmpty()) {
			this.connect(host,port);
		} else {
			this.connect(host,port,password);
		}
	}
	
	public String getServerName(){
		return this.name;
	}
	
	public void setServerName(String newName){
		this.name = newName;
	}
	
	public String getNickname(){
		return this.getName();
	}
	
	public void setNickname(String nick){
		this.setName(nick);
	}
	
	public String getHost(){
		return this.host;
	}
	
	public boolean useSSL(){
		return this.useSSL;
	}
	
	public void setPassword(String passwd){
		this.password = passwd;
	}
	
}
