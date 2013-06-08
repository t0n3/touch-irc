package org.touchirc.activity;

import java.util.Set;
import java.util.TreeSet;

import org.pircbotx.User;
import org.touchirc.R;
import org.touchirc.adapter.UsersListAdapter;
import org.touchirc.irc.IrcBinder;
import org.touchirc.irc.IrcService;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ListView;

public class ChannelUsersActivity extends ListActivity implements ServiceConnection{

	private IrcService ircService;
	private ListView usersLV;
	private Set<User> userSet = new TreeSet<User>();
	private UsersListAdapter ulAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.ircService = null;
		Intent intent = new Intent(this, IrcService.class);
		getApplicationContext().bindService(intent, this, 0);

		setContentView(R.layout.listview_layout);
		this.usersLV = (ListView) findViewById(android.R.id.list);

		this.usersLV.setPadding(10, 10, 10, 10);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		this.ircService = null;

	}


	@Override
	public void onServiceConnected(ComponentName name, IBinder binder) {
		this.ircService = ((IrcBinder) binder).getService();
		// Collect all users connected to the current server
		this.userSet = this.ircService.getCurrentChannel().getUsers();
		System.out.println("set'size : " + this.userSet.size()); // the getUsers() method returns 0 (???)
		this.ulAdapter = new UsersListAdapter(userSet.toArray(new User[0]), getApplicationContext());
		this.usersLV.setAdapter(this.ulAdapter);
		
	}
}
