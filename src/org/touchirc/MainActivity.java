package org.touchirc;

import org.touchirc.irc.IrcBinder;
import org.touchirc.irc.IrcService;
import org.touchirc.model.Server;
import org.touchirc.view.ConversationActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements ServiceConnection {
	private IrcBinder ircServiceBind;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Delete this when we will have thread
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy); 
		
		setContentView(R.layout.activity_main);
		
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),ConversationActivity.class);
				startActivity(i);
			}
		});
		
		Intent intent = new Intent(this, IrcService.class);
		getApplicationContext().startService(intent);
		getApplicationContext().bindService(intent, this, 0);
		
	    
		
	}
	
	private void addServer(){
		Server serverIrc = new Server("Recycled", "irc.recycled-irc.net", 6667);
		System.out.println("ID SERVER = " +serverIrc.getId());
		this.ircServiceBind.getService().addServer(serverIrc);
		this.ircServiceBind.getService().connect(serverIrc);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}




	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
		this.ircServiceBind = null;
		
	}


	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		this.ircServiceBind = (IrcBinder) service;
		addServer();

	}

}
