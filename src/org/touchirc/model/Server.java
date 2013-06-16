package org.touchirc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


// TODO Comments :)

public class Server {
	
	private final static String CHARSET_DEFAULT = "UTF-8";
	
	private String name;
	private String host;
	private int port;
	private String charset;	
	private String password = "";
	private Profile profile;
	private boolean autoConnect;
	private ArrayList<String> autoConnectedChannels;
	
	private boolean skipMOTD = true;
	private boolean useSSL;

	private HashMap<String, Conversation> conversations;
	private String lastConversationName;
	
	public Server(String name, String host, int port){
		this(name, host, port, null, CHARSET_DEFAULT);
	}
	
	public Server(String name, String host, int port, String password){
		this(name, host, port, password, CHARSET_DEFAULT);
	}
	
	public Server(String name, String host, int port, String password, String charset){
		this(name, host, port, password, CHARSET_DEFAULT, false);
	}
	
	public Server(String name, String host, int port, String password, String charset, boolean useSSL){
		this.name = name;
		this.host = host;
		this.port = port;
		this.password = password;
		this.charset = charset;
		this.useSSL = Boolean.valueOf(useSSL);
		this.conversations = new HashMap<String, Conversation>();
		this.profile = null;
		this.autoConnect = false;
		this.useSSL = useSSL;
	}
	
	public Conversation getConversation(String title){
		return conversations.get(title);
	}
	
	public ArrayList<String> getAllConversations(){
		ArrayList<String> a = new ArrayList<String>(conversations.keySet());
		Collections.sort(a);
		return a;
	}
	
	public void addConversation(Conversation c){
		this.conversations.put(c.getTitle(),c);
		lastConversationName = c.getTitle();		
	}
	
	public boolean hasConversation(String title){
		System.out.println("search : " +title+ " => "+conversations.containsKey(title));
		return conversations.containsKey(title);
	}
	
	public void removeConversation(String channel) {
		this.conversations.remove(channel);
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
	
	public void setUseSSL(boolean useSSL){
		this.useSSL = useSSL;
	}
	
	public boolean useSSL(){
		return this.useSSL;
	}
	
	public String getCharset() {
		return this.charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public boolean isProtected(){
		return this.password != null; // != null => a password was setted
	}

	public boolean hasConversation(){
		if(this.conversations.isEmpty())
			return false;
		return true;
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
	
	public void enableAutoConnect(){
		this.autoConnect = true;
	}
	
	public void disableAutoConnect(){
		this.autoConnect = false;
	}
	
	public boolean isAutoConnect(){
		return this.autoConnect;
	}

	public void setAutoConnectedChannels(ArrayList<String> channelList) {
		this.autoConnectedChannels = channelList;
	}
	
	public ArrayList<String> getAutoConnectedChannels(){
		return this.autoConnectedChannels;
	}

	public String getLastConversationName(){
		return lastConversationName;
	}
	
}
