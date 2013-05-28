package org.touchirc.fragments;

import java.util.LinkedList;

import org.touchirc.model.Conversation;
import org.touchirc.model.Message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ConversationFragment extends Fragment {
	
	private TextView textView;
	private Conversation conversation;
	private String sConversation;
	private BroadcastReceiver messageReceiver;

	public ConversationFragment(){
		this(new Conversation("null")); // Maybe we should find a trick to avoid this :/
	}
	
	public ConversationFragment(Conversation c){
		this.conversation = c;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		sConversation = "";
		if (savedInstanceState != null)
			sConversation = savedInstanceState.getString("conversation");
		getFormattedConversation(this.conversation.getHistory());

		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		// Construct the TextView
		this.textView = new TextView(container.getContext());
		// Fill the TextView with Conversation
		this.textView.setText(sConversation);
		// Add the TextView to the layout		
		v.addView(this.textView);
		
		this.messageReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				getFormattedConversation(conversation.getBuffer());
				conversation.cleanBuffer();
				textView.setText(sConversation);
			}	
		};
		getActivity().registerReceiver(this.messageReceiver , new IntentFilter("org.touchirc.irc.newMessage"));

		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("conversation", this.sConversation);
	}

	public void getFormattedConversation(LinkedList<Message> buffer){
		for (Message m : buffer) {
			this.sConversation += "<" + m.getAuthor() + "> " + m.getMessage();
			this.sConversation += "\n";
		}
	}
	
}
