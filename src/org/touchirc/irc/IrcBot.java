package org.touchirc.irc;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.touchirc.model.Conversation;
import org.touchirc.model.Message;
import org.touchirc.model.Server;

import android.content.Intent;
import android.util.Log;

public class IrcBot extends PircBotX implements Listener<IrcBot>{
	
	private IrcService service;
	private Server server;

	public IrcBot(Server server, IrcService service) {
		this.service = service;
		this.server = server;
		this.server.addConversation(new Conversation("#Boulet"));
	}

	// Automatic answer at "time"
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.equalsIgnoreCase("time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
		System.out.println("[IRCBOT] Message recu");
		this.server.getConversation("#Boulet").addMessage(new Message(message, sender));
		Intent intent = new Intent();
		intent.setAction("org.touchirc.irc.newMessage");
		service.sendBroadcast(intent);
	}
	
	/**
	 * Set the Nick of the user
	 * 
	 * @param nickName
	 */
	
	public void setNickName(String nickName){
		//TODO Add security : the nickname can't have some caracters
		this.setName(nickName);
	}
	
	/**
     * Set the ident of the user
     *
     * @param ident 
     */
    public void setIdent(String ident)
    {
        this.setLogin(ident);
    }
    
    /**
     * Set the real name of the user
     *
     * @param realName 
     */
    public void setRealName(String realName)
    {
        this.setVersion(realName);
    }
    
    
    protected void onConnect(){
    	Log.i("[IrbBOT]", "Connected");
    }
    
    protected void onDisconnect(){
    	Log.i("[IrbBOT]", "Disonnected");
    }

	@SuppressWarnings("rawtypes")
	@Override
	public void onEvent(Event rawevent) throws Exception {
		
		if(rawevent instanceof JoinEvent){
			JoinEvent event = (JoinEvent) rawevent;
			if(event.getUser().equals(event.getBot().getUserBot()) && this.server.getConversation(event.getChannel().getName()) == null)
				this.server.addConversation(new Conversation(event.getChannel().getName()));
			return;
		}
		if(rawevent instanceof PartEvent){
			PartEvent event = (PartEvent) rawevent;
			if(event.getUser().equals(event.getBot().getUserBot()) && this.server.getConversation(event.getChannel().getName()) != null)
				this.server.removeConversation(event.getChannel().getName());
			return;
		}
		
	}
    
    

}
