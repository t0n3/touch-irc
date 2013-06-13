package org.touchirc.fragments;


import org.touchirc.activity.ConversationActivity;
import org.touchirc.adapter.ExpandableServerAdapter;
import org.touchirc.irc.IrcService;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RelativeLayout;

@SuppressLint("ValidFragment")
public class ConnectedServersFragment extends Fragment {

	private static IrcService ircService;
	private ExpandableServerAdapter serverAdapter;
	
	
	public ConnectedServersFragment(){
		
	}
	
	public ConnectedServersFragment(IrcService service){
		ircService = service;
	}


	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		serverAdapter = new ExpandableServerAdapter(ircService.getConnectedServers(), getActivity());

		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		// Construct the ListView
		ExpandableListView serversLV = new ExpandableListView(getActivity());
		//serversLV.setDivider(null);
		serversLV.setVerticalFadingEdgeEnabled(false);
		//serversLV.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_INSET);
		//serversLV.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
		serversLV.setAdapter(serverAdapter);
		for(int i = 0 ; i < serverAdapter.getGroupCount() ; i++)
			serversLV.expandGroup(i);
		
		serversLV.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				ircService.setCurrentServer(serverAdapter.getGroup(groupPosition));
				((ConversationActivity) getActivity()).setCurrentConversation(childPosition);
				
				return true;
			}
		});
				
		serversLV.setGroupIndicator(new ColorDrawable(android.R.color.transparent));
		//serversLV.setChildIndicator(getResources().getDrawable(R.drawable.ic_menu_mp_holo_light));
		
		v.addView(serversLV);
		return v;
	}

	public ExpandableServerAdapter getAdapter() {
		return serverAdapter;
	}
	
}
