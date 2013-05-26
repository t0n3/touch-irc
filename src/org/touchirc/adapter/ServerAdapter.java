package org.touchirc.adapter;

import org.touchirc.R;
import org.touchirc.model.Server;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ServerAdapter extends BaseAdapter {

	private SparseArray<Server> serversList;
	private Context c;

	public ServerAdapter (SparseArray<Server> servers_AL, Context c){
		this.serversList = servers_AL;
		this.c = c;
	}

	public int getCount() {
		return serversList.size();
	}

	public Server getItem(int position) {
		return serversList.valueAt(position);
	}

	public long getItemId(int position) {
		return serversList.keyAt(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Server s = serversList.valueAt(position); // Collect the server concerned
		if(s == null)
			return null;
		if (v == null){
			LayoutInflater vi = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_list, null);
		}
		
		TextView autoConnect_TV = (TextView) v.findViewById(R.id.textView_DEFAULT);
		
		// Checking if the current server is auto-connected
		if(s.isAutoConnect()){
			autoConnect_TV.setBackgroundResource(R.drawable.object_border);
			autoConnect_TV.setText(R.string.AUTO);
		}
		else{
			// To ensure the fact that when the method is recalled the resources are corrects
			autoConnect_TV.setBackgroundResource(0);
			autoConnect_TV.setText("");
		}

		TextView serverName_TV = (TextView) v.findViewById(R.id.textView_itemName);
		serverName_TV.setText(s.getName());
		
		TextView hostnameServer_TV = (TextView) v.findViewById(R.id.textView_itemSecondInfo);
		hostnameServer_TV.setText(s.getHost());
		
		return v;
	}
}
