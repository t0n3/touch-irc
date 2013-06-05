package org.touchirc.adapter;

import org.pircbotx.User;
import org.touchirc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UsersListAdapter extends BaseAdapter {
	
	private User [] usersList;
	private Context c;

	public UsersListAdapter (User[] userTab, Context c){
		this.usersList = userTab;
		this.c = c;
	}

	@Override
	public int getCount() {
		return this.usersList.length;
	}

	@Override
	public Object getItem(int i) {
		return this.usersList[i];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		User user = this.usersList[position]; // Collect the user concerned
		if(user == null)
			return null;
		if (v == null){
			LayoutInflater vi = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.user_item_list, null);
		}
		
		ImageView status_ImgView = (ImageView) v.findViewById(R.id.imageViewStatus);
		TextView userName_TV = (TextView) v.findViewById(R.id.textViewUserName);
		userName_TV.setText(user.getNick().toString());
		
		status_ImgView.setBackgroundResource(0);
		
		// TODO Add more status & verify existing ones because not sure about the employed methods
		
		// Checking if the current user is not mute
		if(user.getChannelsVoiceIn().contains(user)){
			status_ImgView.setBackgroundResource(android.R.drawable.presence_audio_online);
			return v;
		}
		
		if(user.getChannelsHalfOpIn().contains(user)){
			status_ImgView.setBackgroundResource(android.R.drawable.presence_audio_away);
			return v;
		}
		
		// Checking if the current user is an OP
		if(user.getChannelsOpIn().contains(user)){
			status_ImgView.setBackgroundResource(android.R.drawable.presence_online);
			return v;
		}
		
		return v;		
	}

}
