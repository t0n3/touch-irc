package org.touchirc.activity;

import org.touchirc.R;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends SherlockActivity{
	private Button create_profile_button;
	private Button existing_profiles_button;
	private Button create_server_button;
	private Button existing_servers_button;
	private Button chat_button;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemQuit :
			
			// Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);

			// Chain together various setter methods to set the dialog characteristics
			builder.setTitle(R.string.exit)
			.setMessage(R.string.QuitAppQuestion)
			.setIcon(android.R.drawable.ic_menu_help);

			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					finish();
				}
			});
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});


			// Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
