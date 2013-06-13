package org.touchirc.fragments;


import java.util.Set;

import org.touchirc.R;
import org.touchirc.irc.IrcService;
import org.touchirc.model.Server;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ConnectedServersFragment extends Fragment {

	private IrcService ircService;
	private ExpandableListView serversLV;
	private ServerAdapter serverAdapter;
	
	public ConnectedServersFragment(IrcService service){
		ircService = service;
	}


	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		serverAdapter = new ServerAdapter(this.ircService.getConnectedServers());

		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		// Construct the ListView
		serversLV = new ExpandableListView(getActivity());
		serversLV.setDivider(null);
		serversLV.setVerticalFadingEdgeEnabled(false);
		serversLV.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_INSET);
		serversLV.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
		serversLV.setAdapter(serverAdapter);
		for(int i = 0 ; i < serverAdapter.getGroupCount() ; i++)
			serversLV.expandGroup(i);
		// TODO Add a on click listener :)		
		// Add the ListView to the layout	
		
		v.addView(serversLV);
		
		
		
		return v;
	}
	
	
	
	
	
	
	
	public class ServerAdapter extends BaseExpandableListAdapter {

		private Server[] serversList;
		

		private LayoutInflater inflater;

		public ServerAdapter (Set<Server> set){
			this.serversList = set.toArray(new Server[0]);
	        inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public String getChild(int groupPosition, int childPosition) {
			return this.serversList[groupPosition].getAllConversations().get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
			TextView channel;
			
			if(convertView == null)
				convertView = inflater.inflate(R.layout.user_item_list, null);
			channel = (TextView) convertView.findViewById(R.id.textViewUserName);
			channel.setText(getChild(groupPosition, childPosition));
			 
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this.serversList[groupPosition].getAllConversations().size();
		}

		@Override
		public Server getGroup(int groupPosition) {
			return this.serversList[groupPosition];
		}

		@Override
		public int getGroupCount() {
			return this.serversList.length;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			
			ServerViewHolder serverHolder;
			Server server = getGroup(groupPosition);
			if (convertView == null) {
				serverHolder = new ServerViewHolder();
				convertView = inflater.inflate(R.layout.item_list, null);
			 
				serverHolder.serverName = (TextView) convertView.findViewById(R.id.textView_itemName);
				serverHolder.hostName = (TextView) convertView.findViewById(R.id.textView_itemSecondInfo);
				convertView.setTag(serverHolder);
			} else {
				serverHolder = (ServerViewHolder) convertView.getTag();
			}
			 
			serverHolder.serverName.setText(server.getName());
			serverHolder.hostName.setText(server.getHost());
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
		
		class ServerViewHolder {
			public TextView serverName;
			public TextView hostName;
		}
	}

}
