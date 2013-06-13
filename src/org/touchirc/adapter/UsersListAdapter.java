package org.touchirc.adapter;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.touchirc.R;
import org.touchirc.irc.IrcService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UsersListAdapter extends BaseAdapter {
	
	private IrcService ircService;
	private Context c;

	public UsersListAdapter (IrcService ircService, Context c){
		this.ircService = ircService;
		this.c = c;
	}

	@Override
	public int getCount() {
		return ircService.getCurrentChannel().getUsers().size();
	}

	@Override
	public User getItem(int i) {
		return ircService.getCurrentChannel().getUsers().toArray(new User[0])[i];
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
		
		// Checking if the current user is an OP
		Channel channel = ircService.getCurrentChannel();
		if(user.getChannelsOpIn().contains(channel)){
			status_ImgView.setBackgroundResource(android.R.drawable.presence_online);
			return convertView;
		}
		if(user.getChannelsHalfOpIn().contains(channel)){
			status_ImgView.setBackgroundResource(android.R.drawable.presence_audio_away);
			return convertView;
		}
		if(user.getChannelsVoiceIn().contains(channel)){
			status_ImgView.setBackgroundResource(android.R.drawable.presence_audio_online);
			return convertView;
		}
		
		return convertView;		
	}

}
