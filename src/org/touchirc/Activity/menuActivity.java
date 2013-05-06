package org.touchirc.activity;

import org.touchirc.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class menuActivity extends Activity{
	private Button create_profile_button;
	private Button existing_profiles_button;
	private Button create_server_button;
	private Button existing_servers_button;
	private Button chat_button;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.menu);

		// Buttons

		this.create_profile_button = (Button) findViewById(R.id.createProfileBT);

		this.existing_profiles_button = (Button) findViewById(R.id.existingProfileBT);
		
		this.create_server_button = (Button) findViewById(R.id.createServerBT);
		
		this.existing_servers_button = (Button) findViewById(R.id.existingServerBT);
		
		this.chat_button = (Button) findViewById(R.id.ChatBT);
		
		this.create_profile_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentProfile = new Intent(getApplicationContext(), CreateProfileActivity.class);
				intentProfile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(intentProfile);			
			}
		});
		
		this.existing_profiles_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentProfile = new Intent(getApplicationContext(), ExistingProfilesActivity.class);
				intentProfile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(intentProfile);
			}
		});	
		
		this.create_server_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentServer = new Intent(getApplicationContext(), CreateServerActivity.class);
				intentServer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(intentServer);			
			}
		});
		
		this.existing_servers_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentProfile = new Intent(getApplicationContext(), ExistingServersActivity.class);
				intentProfile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(intentProfile);			
			}
		});	
		
		this.chat_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intentChat = new Intent(getApplicationContext(), ConversationActivity.class);
				intentChat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(intentChat);			
			}
		});
	}
}
