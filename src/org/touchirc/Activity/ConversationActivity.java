package org.touchirc.view;

import java.util.LinkedList;

import org.touchirc.R;
import org.touchirc.irc.IrcBinder;
import org.touchirc.irc.IrcService;
import org.touchirc.model.Conversation;
import org.touchirc.model.Message;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ConversationActivity extends ListActivity implements ServiceConnection {
	private IrcBinder ircServiceBind;
	private BroadcastReceiver MessageReceiver;
	private Conversation conversation;
	private EditText input;
	private ListView list;
	private LinkedList<Message> values;
	private ArrayAdapter<Message> adapter;


	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.conversation_display);
		this.values = new LinkedList<Message>();
		this.input = (EditText) findViewById(R.id.messageToSend);
		this.list = (ListView) findViewById(android.R.id.list);
		list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		this.input.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEND) {
		            sendMessage();
		            return true;
		        }
		        return false;
			}
		});

		
		
		
		
		adapter = new ArrayAdapter<Message>(this,	android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
		
		/*			/--------- TODO ----------\
		 * 
		final EditText editText = (EditText) findViewById(R.id.messageToSend);
		editText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEND) {
		            values.add(new Message("You", editText.getText().toString(), 0));
		            adapter.notifyDataSetChanged();
		            handled = true;
		        }
		        return handled;
			}
		});
		*/
		
		Intent intent = new Intent(this, IrcService.class);
		// getApplicationContext().startService(intent);
		getApplicationContext().bindService(intent, this, 0);
		
		this.MessageReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				LinkedList<Message> buffer = conversation.getBuffer();
				for(Message m : buffer){
					values.add(m);
				}
				conversation.cleanBuffer();
				adapter.notifyDataSetChanged();
				
			}	
		};
		registerReceiver(this.MessageReceiver , new IntentFilter("org.touchirc.irc.newMessage"));


	}
	
	public void sendMessage(){
		String msg = input.getText().toString();
		input.setText("");
		
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
		this.conversation = this.ircServiceBind.getService().getServerById(0).getConversation("#Boulet");

	}

}
