package org.touchirc.adapter;

import java.util.Set;

import org.touchirc.R;
import org.touchirc.model.Server;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableServerAdapter extends BaseExpandableListAdapter {

	private Server[] serversList;
	

	private LayoutInflater inflater;

	public ExpandableServerAdapter (Set<Server> set, Context c){
		this.serversList = set.toArray(new Server[0]);
        inflater = LayoutInflater.from(c);
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
			convertView = inflater.inflate(R.layout.connected_channel_item, null);
		channel = (TextView) convertView.findViewById(R.id.channelItemTextView);
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
		
		TextView server;
		if(convertView == null)
			convertView = inflater.inflate(R.layout.connected_server_item, null);
		server = (TextView) convertView.findViewById(R.id.serverItemTextView);
		server.setText(getGroup(groupPosition).getName());

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
}
