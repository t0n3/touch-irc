package org.touchirc.irc;


import java.io.IOException;
import java.util.HashMap;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.touchirc.model.Server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class IrcService extends Service {
	
	private final IrcBinder ircBinder;
	private HashMap<Integer, IrcBot> bots;
	private HashMap<Integer, Server> servers;
	
	public IrcService(){
		super();
		this.ircBinder = new IrcBinder(this);
		this.bots = new HashMap<Integer, IrcBot>();
		this.servers = new HashMap<Integer, Server>();
	}
	
	@Override
	public void onCreate(){
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return this.ircBinder;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Toast.makeText(getApplicationContext(), "TOTO", Toast.LENGTH_LONG).show();
		System.out.println("TOTO");
		return START_STICKY;
	
	}
	
	//TODO Is the service have to do that ?
	public void addServer(Server server){
		this.servers.put(server.getId(),server);
	}
	
	//TODO Is the service have to do that ?
	public Server getServerById(int idServer){
		//return null if the idServer isn't in the Hashmap servers
		return this.servers.get(idServer);
	}
	
	public void connect(final Server server){
		final IrcBot bot = getBot(server.getId());
		
		new Thread("Thread for the server : " + server.getName()){
			@Override
			public void run(){
				
				bot.setNickName("Toto1234");
				bot.setIdent("BouletIdent");
				bot.setRealName("BouletRealName");
				
				
				try {
					bot.setEncoding(server.getEncoding());
					bot.connect(server.getHost(),server.getPort(),server.getPassword());
					
					bot.joinChannel("#Boulet");
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
			}
		}.start();
		
		
		
	}
	
	/**
	 * Get the Bot/Connection of the server with this idServer
	 * @param idServer of the server (= id of the Bot)
	 * @return the bot
	 */
	// If the Bot don't existe it will be created !
	public synchronized IrcBot getBot(int idServer){
		IrcBot ircBot = this.bots.get(idServer);
		if(ircBot == null){
			ircBot = new IrcBot(idServer,this);
			this.bots.put(idServer, ircBot);
		}
		return ircBot;
	}
	
	public void toto(){
		
	}
	
   

   

    
}