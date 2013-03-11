package org.tone.touchirc;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import android.content.Context;
import android.widget.Toast;

public class MyIrcBot extends PircBot {

	public MyIrcBot(Context context) {
		// DELETE THIS : Debug mode
		this.setVerbose(true);
		
		// 
		this.setName("Toto5344");
		//this.changeNick("Toto5344yh");
		
		try {
			this.connect("irc.freenode.net");
		} catch (NickAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.joinChannel("#Boulet");
	}

	// Automatic answer at "time"
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.equalsIgnoreCase("time")) {
			String time = new java.util.Date().toString();
			sendMessage(channel, sender + ": The time is now " + time);
		}
	}

}
