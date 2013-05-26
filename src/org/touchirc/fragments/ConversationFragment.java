package org.touchirc.fragments;

import org.touchirc.R;
import org.touchirc.model.Conversation;
import org.touchirc.model.Message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConversationFragment extends Fragment {
	
	private TextView textView;
	private String conversation;

	public ConversationFragment(){
		this(new Conversation("null")); // Maybe we should find a trick to avoid this :/
	}
	
	public ConversationFragment(Conversation c){
		this.conversation = getFormattedConversation(c);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null)
			this.conversation = savedInstanceState.getString("conversation");
		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		// Construct the TextView
		this.textView = new TextView(container.getContext());
		// Fill the TextView with Conversation
		this.textView.setText(this.conversation);
		// Add the TextView to the layout		
		v.addView(this.textView);

		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("conversation", this.conversation);
	}

	public String getFormattedConversation(Conversation c){
		String s = "";
		for (Message m : c.getHistory()) {
			s += "<" + m.getAuthor() + "> " + m.getMessage();
			s += "\n";
		}
		return s;
	}
	
}
