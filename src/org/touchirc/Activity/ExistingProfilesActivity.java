package org.touchirc.activity;

import java.util.ArrayList;

import org.touchirc.R;
import org.touchirc.R.layout;
import org.touchirc.db.Database;
import org.touchirc.model.Profile;
import org.touchirc.model.ProfileAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExistingProfilesActivity extends ListActivity{

	private TextView profiles_TV;
	private ListView profiles_LV;
	private ArrayList<Profile> profiles_AL;
	private ProfileAdapter adapterProfile;
	private int index;
	private Context c;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(layout.listview_layout);

		// Collect the context
		c = this;

		// Collect the profile widgets TextView (TV), ListView (LV)
		this.profiles_TV = (TextView) findViewById(R.id.TV_Objects_name);
		this.profiles_LV = (ListView) findViewById(android.R.id.list);

		// the LV is always focused on its last item
		this.profiles_LV.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		// Collect the Profiles list
		this.profiles_AL = new Database(getApplicationContext()).getProfileList();

		profiles_TV.setText("Profiles List :       (" + this.profiles_AL.size() + ")");

		// Put an ProfileAdapter so that the LV and the profiles list be linked
		this.adapterProfile = new ProfileAdapter(profiles_AL, c);
		this.setListAdapter(adapterProfile);

		registerForContextMenu(profiles_LV);
	}
	
	/**
	 * When the user long click on a View of the ListView, he triggers
	 * a ContextMenu which suggest him "Defaulting", Editing or Removing the current profile
	 */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		// Load the context Menu
		inflater.inflate(R.menu.context_menu_profile, menu);
		// Give a title to the ContextMenu
		menu.setHeaderTitle("Options profile");
		
		// Collecting the name of the selected profile, using a AdapterContextMenuInfo
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

		// Put all the profile's name in an Array
		String [] profiles = new String [profiles_AL.size()];
		for(int p = 0 ; p < profiles_AL.size() ; p++){
			profiles[p] = profiles_AL.get(p).getProfile_name();
		}

		// Collecting the name of the selected profile
		String selectedProfile_name = profiles[(int)info.id];
		
		// if the profile is already by default, we cannot set it by default
		if(selectedProfile_name.equals(new Database(c).nameDefaultProfile())){
			menu.getItem(0).setEnabled(false);
		}
	}
	
	/**
	 * According the Item selected ("Set by Default", "Edit", "Delete")
	 * specific statements will be done
	 * 
	 */

	public boolean onContextItemSelected(MenuItem item) {

		// Collecting the name of the selected profile, using a AdapterContextMenuInfo
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		// Put all the profile's name in an Array
		String [] profiles = new String [profiles_AL.size()];
		for(int p = 0 ; p < profiles_AL.size() ; p++){
			profiles[p] = profiles_AL.get(p).getProfile_name();
		}

		// Collecting the name and position of the selected profile
		String selectedProfile_name = profiles[(int)info.id];
		int selectedProfile_position = (int)info.id;
		this.index = selectedProfile_position;

		// if the item "Set By Default" is selected
		if(item.getItemId() == R.id.setByDefault){

			Database db = new Database(c);
			// The selected profile is now the default profile
			if(db.setDefaultProfile(selectedProfile_name)){
				Toast.makeText(c, selectedProfile_name + " is now the profile by default !", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(c, "An error occurred while setting the profile by default :(", Toast.LENGTH_LONG).show();
			}
			
			db.close();
			
			// Notifying the adapter to update the display
			adapterProfile.notifyDataSetInvalidated();
			
			return true;
		}
		
		// if the item "Edit" is selected		
		if(item.getItemId() == R.id.edit){

			// Collect all the informations concerning the current profile
			Database db = new Database(c);
			Profile profileToEdit = db.getProfileByName(selectedProfile_name);
			
			// Prepare the Intent to switch on the activity which allows to modify the profile
			Intent i = new Intent(this, CreateProfileActivity.class);

			// Put the informations of the current profile into a Bundle object
			Bundle b = new Bundle();
			b.putString("ProfileName", profileToEdit.getProfile_name());
			b.putString("FirstNickName", profileToEdit.getFirstNick());
			b.putString("ScdNickName", profileToEdit.getSecondNick());
			b.putString("ThdNickName", profileToEdit.getThirdNick());
			b.putString("UserName", profileToEdit.getUsername());
			b.putString("RealName", profileToEdit.getRealname());
			
			// Assign the Bundle to the Intent
			i.putExtra("ProfileIdentification", b);

			// Start the Intent
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			
			db.close();
			
			finish();
			
			return true;
		}
		
		// if the item "Delete" is selected
		if(item.getItemId() == R.id.delete){

			// Removal throughout the db
			Database db = new Database(c);

			if (db.deleteProfile(selectedProfile_name)){ // if the deletion is successful
				// Notify the user thanks to a Toast
				Toast.makeText(c, "Profile " + selectedProfile_name + " deleted.", Toast.LENGTH_LONG).show();
				db.close();
				// Remove the corresponding profile from the list
				profiles_AL.remove(selectedProfile_position);
				// Notify the adapter that the list's state has changed
				adapterProfile.notifyDataSetChanged();
				// Update the number of available profiles in the TV
				profiles_TV.setText("Profiles List :       (" + this.profiles_AL.size() + ")");
			}
			
			db.close();

			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@ Override
	protected void onResume(){
		super.onResume();
		// Use a Bundle to collect the new name of the profile
		Bundle b = this.getIntent().getBundleExtra("NewValue");
		if(b != null && b.containsKey("NewValue")){
			String nameProfile = b.getString("NewNameProfile");
			this.profiles_AL.get(index).setProfile_name(nameProfile);

			// Update the list and its display
			this.adapterProfile.notifyDataSetChanged();
			this.adapterProfile.notifyDataSetInvalidated();
		}
	}
}
