package org.touchirc.adapter;

import java.util.HashMap;

import org.touchirc.R;
import org.touchirc.db.Database;
import org.touchirc.model.Profile;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProfileAdapter extends BaseAdapter {

	private SparseArray<Profile> profilesList;
	private Context c;

	public ProfileAdapter (SparseArray<Profile> profiles_AL, Context c){
		this.profilesList = profiles_AL;
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
		
		TextView default_TV = (TextView) v.findViewById(R.id.textView_DEFAULT);
		
		// Checking if the current profile is by default
		Database db = new Database(c);
		if(db.nameDefaultProfile() != null && db.nameDefaultProfile().equals(p.getProfile_name())){
			default_TV.setBackgroundResource(R.drawable.object_border);
			default_TV.setText(R.string.DEFAULT);
		}
		else{
			// To ensure the fact that when the method is recalled the resources are corrects
			default_TV.setBackgroundResource(0);
			default_TV.setText("");
		}
		db.close();
		
		TextView profileName_TV = (TextView) v.findViewById(R.id.textView_itemName);
		profileName_TV.setText(p.getProfile_name());
		
		TextView firstNickName_TV = (TextView) v.findViewById(R.id.textView_itemSecondInfo);
		firstNickName_TV.setText(p.getFirstNick());
		
		return v;
	}
}