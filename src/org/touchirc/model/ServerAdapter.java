package org.touchirc.model;

import java.util.ArrayList;

import org.touchirc.R;
import org.touchirc.db.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
			v = vi.inflate(R.layout.item_list_profile, null);
		}
		
		ImageView defaultServer_IMG = (ImageView) v.findViewById(R.id.profileStatus_IMGV);
		
		// Checking if the current server is auto-connected
		Database db = new Database(c);
		if(db.nameAutoConnectedServer() != null && db.nameAutoConnectedServer().equals(s.getName())){ // if positive we change the IMGView
			// Changing the ImageView
			defaultServer_IMG.setImageResource(android.R.drawable.presence_online);
		}
		else{
			// To ensure the fact that when the method is recalled the src is correct
			defaultServer_IMG.setImageResource(android.R.drawable.presence_offline);
		}
		db.close();

		TextView profileName_TV = (TextView) v.findViewById(R.id.profileName_TV);
		profileName_TV.setText(s.getName());                            
		return v;
	}
}
