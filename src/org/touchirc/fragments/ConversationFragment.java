package org.touchirc.fragments;

import java.util.LinkedList;

import org.touchirc.adapter.MessageAdapter;
import org.touchirc.model.Conversation;
import org.touchirc.model.Message;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ConversationFragment extends Fragment {
	
	private ListView listView;
	private Conversation conversation;
	private MessageAdapter messageAdapter;
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
		
		messageAdapter = new MessageAdapter(this.conversation, getActivity());

		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		// Construct the ListView
		listView = new ListView(container.getContext());
		listView.setDivider(null);
		listView.setVerticalFadingEdgeEnabled(false);
		listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_INSET);
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
		listView.setPadding(5, 0, 5, 0);
		listView.setAdapter(messageAdapter);
		// TODO Add a on click listener :)		
		// Add the ListView to the layout		
		v.addView(listView);
		
		// Broadcast : actions to execute when a new message pop
		this.messageReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				messageAdapter.addMessages(conversation.getBuffer());
				conversation.cleanBuffer();
			}	
		};
		getActivity().registerReceiver(this.messageReceiver , new IntentFilter("org.touchirc.irc.newMessage"));
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(this.messageReceiver , new IntentFilter("org.touchirc.irc.newMessage"));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(messageReceiver);
	}
	
}
