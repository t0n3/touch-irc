package org.touchirc.adapter;

import java.util.Set;

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

	public UsersListAdapter (Set<User> set, Context c){
		this.usersList = set.toArray(new User[0]);
		this.c = c;
	}

	@Override
	public int getCount() {
		return this.usersList.length;
	}

	@Override
	public User getItem(int i) {
		return this.usersList[i];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(c).inflate(R.layout.user_item_list, null);
		
		User user = getItem(position);
		ImageView status_ImgView = (ImageView) convertView.findViewById(R.id.imageViewStatus);
		TextView userName_TV = (TextView) convertView.findViewById(R.id.textViewUserName);
		userName_TV.setText(user.getNick().toString());
		
		status_ImgView.setBackgroundResource(0);
		
		// TODO Add more status & verify existing ones because not sure about the employed methods
		
		// Checking if the current user is an OP
		if(user.getChannelsOpIn().contains(user)){
			status_ImgView.setBackgroundResource(android.R.drawable.presence_online);
			return convertView;
		}
		if(user.getChannelsHalfOpIn().contains(user)){
			status_ImgView.setBackgroundResource(android.R.drawable.presence_audio_away);
			return convertView;
		}
		if(user.getChannelsVoiceIn().contains(user)){
			status_ImgView.setBackgroundResource(android.R.drawable.presence_audio_online);
			return convertView;
		}
		
		return convertView;		
	}

}
