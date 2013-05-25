package org.touchirc.model;

import java.util.HashMap;


// TODO Comments :)

public class Server {
	
	private final static String CHARSET_DEFAULT = "UTF-8";
	
	private int id;
	private String name;
	private String host;
	private int port;
	private String charset;	
	private String password = "";
	private Profile profile;
	
	private boolean skipMOTD = true;
	// TODO Add SSL support
	private boolean useSSL = false;

	private HashMap<String, Conversation> conversations;
	
	public Server(String name, String host, int port){
		this(name, host, port, null, CHARSET_DEFAULT);
	}
	
	public Server(String name, String host, int port, String password){
		this(name, host, port, password, CHARSET_DEFAULT);
	}
	
	public Server(String name, String host, int port, String password, String charset){
		this.name = name;
		this.host = host;
		this.port = port;
		this.password = password;
		this.charset = charset;
		this.conversations = new HashMap<String, Conversation>();
		this.profile = null;
	}
	
	public Conversation getConversation(String title){
		return this.conversations.get(title);
	}
	
	public void addConversation(Conversation c){
		this.conversations.put(c.getTitle(),c);
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String newName){
		this.name = newName;
	}
	
	public String getNick(){
		return this.getName();
	}
	
	public void setNick(String nick){
		this.setName(nick);
	}
	
	public String getHost(){
		return this.host;
	}

	public void setHost(String newHost){
		this.host = newHost;
	}

	public int getPort(){
		return this.port;
	}

	public void setPort(int newPort){
		this.port = newPort;
	}

	public String getPassword(){
		return this.password;
	}

	public void setPassword(String newPassword){
		this.password = newPassword;
	}

	public String getEncoding(){
		return this.charset;
	}

	public void setEncoding(String newCharset){
		this.charset = newCharset;
	}
	
	public boolean useSSL(){
		return this.useSSL;
	}
	
	public boolean isProtected(){
		return this.password != null; // != null => a password was setted
	}
	
	public void setProfile(Profile p){
		this.profile = p;
	}
	
	public void deleteProfile(){
		this.profile = null;
	}
	
	public boolean hasAssociatedProfile(){
		return this.profile != null;
	}
	
	public Profile getProfile(){
		return this.profile;
	}
}
