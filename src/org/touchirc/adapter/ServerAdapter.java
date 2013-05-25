package org.touchirc.adapter;

import java.util.ArrayList;

import org.touchirc.R;
import org.touchirc.db.Database;
import org.touchirc.model.Server;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ServerAdapter extends BaseAdapter {

	private ArrayList<Server> serversList;
	private Context c;

	public ServerAdapter (ArrayList<Server> data, Context c){
		this.serversList = data;
		this.c = c;
	}

	public int getCount() {
		return serversList.size();
	}

	public Object getItem(int position) {
		return serversList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Server s = serversList.get(position); // Collect the server concerned
		if (v == null){
			LayoutInflater vi = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_list, null);
		}
		
		TextView autoConnect_TV = (TextView) v.findViewById(R.id.textView_DEFAULT);
		
		// Checking if the current server is auto-connected
		Database db = new Database(c);
		if(db.nameAutoConnectedServer() != null && db.nameAutoConnectedServer().equals(s.getName())){
			autoConnect_TV.setBackgroundResource(R.drawable.object_border);
			autoConnect_TV.setText(R.string.AUTO);
		}
		else{
			// To ensure the fact that when the method is recalled the resources are corrects
			autoConnect_TV.setBackgroundResource(0);
			autoConnect_TV.setText("");
		}
		db.close();

		TextView serverName_TV = (TextView) v.findViewById(R.id.textView_itemName);
		serverName_TV.setText(s.getName());
		
		TextView hostnameServer_TV = (TextView) v.findViewById(R.id.textView_itemSecondInfo);
		hostnameServer_TV.setText(s.getHost());
		
		return v;
	}
}
