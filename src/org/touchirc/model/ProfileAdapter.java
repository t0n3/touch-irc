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

public class ProfileAdapter extends BaseAdapter {

	private ArrayList<Profile> profilesList;
	private Context c;

	public ProfileAdapter (ArrayList<Profile> data, Context c){
		this.profilesList = data;
		this.c = c;
	}

	public int getCount() {
		return profilesList.size();
	}

	public Object getItem(int position) {
		return profilesList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		Profile p = profilesList.get(position); // Collect the profile concerned
		if (v == null){
			LayoutInflater vi = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.item_list, null);
		}
		
		ImageView defaultProfile_IMG = (ImageView) v.findViewById(R.id.imageView_itemStatus);
		
		// Checking if the current profile is by default
		Database db = new Database(c);
		if(db.nameDefaultProfile() != null && db.nameDefaultProfile().equals(p.getProfile_name())){ // if positive we change the IMGView
			// Changing the ImageView
			defaultProfile_IMG.setImageResource(android.R.drawable.presence_online);
		}
		else{
			// To ensure the fact that when the method is recalled the src is correct
			defaultProfile_IMG.setImageResource(android.R.drawable.presence_offline);
		}
		db.close();
		
		TextView profileName_TV = (TextView) v.findViewById(R.id.textView_itemName);
		profileName_TV.setText(p.getProfile_name() + " - Nick : " + p.getFirstNick());                            
		return v;
	}
}