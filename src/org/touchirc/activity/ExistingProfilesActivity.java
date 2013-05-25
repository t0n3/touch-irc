package org.touchirc.activity;

import java.util.ArrayList;

import org.touchirc.R;
import org.touchirc.R.layout;
import org.touchirc.adapter.ProfileAdapter;
import org.touchirc.db.Database;
import org.touchirc.model.Profile;
import org.touchirc.model.Server;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ExistingProfilesActivity extends SherlockListActivity {

	private ListView profiles_LV;
	private ArrayList<Profile> profiles_AL;
	private ProfileAdapter adapterProfile;
	private int indexSelectedItem; // selected profile's index in the listView
	private String nameSelectedItem; // selected profile's name
	private Context c;
	private ActionBar actionBar;

	protected ActionMode mActionMode; // Variable used for triggering the actionMode (ActionBar)
	private static View oldView; // Variable used to store the old view selected
	private static View currentView; // Variable used to store the current view selected
	protected boolean subMenuLinkCreated = false; // Variable used to warn OnPrepare method first display ActionBar

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// The icon App can do "Previous"
		this.actionBar = getSupportActionBar();
		this.actionBar.setDisplayHomeAsUpEnabled(true);

		setContentView(layout.listview_layout);

		// Collect the context
		c = this;

		// Collect the profile widgets ListView (LV)
		this.profiles_LV = (ListView) findViewById(android.R.id.list);

		// the LV is always focused on its last item
		this.profiles_LV.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		// Collect the Profiles list
		this.profiles_AL = new Database(getApplicationContext()).getProfileList();

		this.actionBar.setTitle("Profiles  (" + this.profiles_AL.size() + ")");

		// Put an ProfileAdapter so that the LV and the profiles list be linked
		this.adapterProfile = new ProfileAdapter(profiles_AL, c);
		this.setListAdapter(adapterProfile);

		/**
		 * 
		 * When the user click on an item an ActionMode Bar appears.
		 * It provides him some features on the selected profile : Set by Default, Edit, Link a Server, Delete.
		 * 
		 */

		profiles_LV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v,
					int position, long arg3) {

				indexSelectedItem = position;
				currentView = v;

				// if the ActionMode is already displayed
				if (mActionMode != null) {

					oldView.setBackgroundColor(profiles_LV.getCacheColorHint());
					mActionMode.finish(); // closing it
					mActionMode = startActionMode(new ActionModeProfileSettings()); // and launching it to update values
					v.setBackgroundColor(Color.GRAY); // the selected item in the listView is highlighted
					oldView = v;
				}
				else{

					// Start the CallBackActionBar using the ActionMode.Callback defined below
					mActionMode = startActionMode(new ActionModeProfileSettings()); // launching the ActionMode
					oldView = v;
					v.setSelected(true);
					v.setBackgroundColor(Color.GRAY); // the selected item in the listView is highlighted
				}
			}
		});
	}

	/**
	 * 
	 * Here, we define the behavior of the actionMode according the input events
	 * thanks to a specific class
	 * 
	 */

	private final class ActionModeProfileSettings implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			MenuInflater inflater = getSupportMenuInflater();

			// Load the contextual Menu
			inflater.inflate(R.menu.context_menu_profile, menu);

			// Put all the profile's name in an Array, in order to ...
			String [] profiles = new String [profiles_AL.size()];
			for(int p = 0 ; p < profiles_AL.size() ; p++){
				profiles[p] = profiles_AL.get(p).getProfile_name();
			}

			// ... collect the name of the selected profile
			nameSelectedItem = profiles[indexSelectedItem];

			// if the profile is already by default, we cannot set it by default
			// the icon is updated
			if(nameSelectedItem.equals(new Database(c).nameDefaultProfile())){
				menu.getItem(2).setIcon(android.R.drawable.star_on);
				menu.getItem(2).setEnabled(false);
			}
			else{
				menu.getItem(2).setIcon(android.R.drawable.star_off);
			}

			// If there is no server in the database, we disable the button "Link A Server"
			Database db = new Database(c);
			final ArrayList<Server> servers = db.getServerList();

			if(servers.size() == 0){
				menu.getItem(3).setEnabled(false);
			}

			// the subMenu providing the available servers is not still created
			subMenuLinkCreated = false; 

			return true;
		}
		
		/**
		 * 
		 * As this method is called after the onCreateActionMode, it will check the links ; but ...
		 * to check the links, the menu (in parameters) needs its subMenu (and its subItems) but 
		 * they are not created (only after clicking at the item "Link a Server").
		 * So, we have to "wait" that the user click at this item for calling onPrepareActionMode,
		 * which will be called each time a link is added/removed by the user. 
		 * 
		 */

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {		
			// if the subMenu is created, we can check the links between profiles & servers
			if(subMenuLinkCreated){
				checkingLinkBetweenProfileAndServers(menu);
			}
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			Database db = new Database(c);

			switch (item.getItemId()) {

			// ########## if the item "Edit" is selected ##########		
			case R.id.edit : 

				// Collect all the informations concerning the current profile
				Profile profileToEdit = db.getProfileByName(nameSelectedItem);

				// Prepare the Intent to switch on the activity which allows to modify the profile
				Intent i = new Intent(ExistingProfilesActivity.this, CreateProfileActivity.class);

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

				finish(); // close the current activity

				db.close(); // close the database
				return true;

			// ########## if the item "Delete" is selected ##########
			case R.id.delete :

				// Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(ExistingProfilesActivity.this);

				// Chain together various setter methods to set the dialog characteristics
				builder.setTitle(R.string.deleteProfile)
				.setMessage("Would you really like to delete the profile : " + nameSelectedItem + " ?")
				.setIcon(android.R.drawable.ic_menu_delete);

				final ActionMode am = mode;

				builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Removal throughout the db
						if (new Database(c).deleteProfile(nameSelectedItem)){ // if the deletion is successful
							// Notify the user thanks to a Toast
							Toast.makeText(c, "Profile " + nameSelectedItem + " deleted.", Toast.LENGTH_LONG).show();
							// Remove the corresponding profile from the list
							profiles_AL.remove(indexSelectedItem);
							// Notify the adapter that the list's state has changed
							adapterProfile.notifyDataSetChanged();
							// Notifying the adapter to update the display
							adapterProfile.notifyDataSetInvalidated();
							// Update the number of available profiles in the actionBar
							actionBar.setTitle("Profiles  (" + profiles_AL.size() + ")");
						}
						am.finish();
					}
				});
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});


				// Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();

				db.close();
				return true;

			// ########## if the item "Set By Default" is selected ##########
			case R.id.setByDefault :

				// The selected profile is now the default profile
				if(db.setDefaultProfile(nameSelectedItem)){
					mode.getMenu().getItem(2).setIcon(android.R.drawable.star_on);
					mode.getMenu().getItem(2).setEnabled(false);
					Toast.makeText(c, nameSelectedItem + " is now the profile by default !", Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(c, "An error occurred while setting the profile by default :(", Toast.LENGTH_LONG).show();
				}

				// Notifying the adapter to update the display
				adapterProfile.notifyDataSetInvalidated();

				db.close(); // close the database
				return true;


			// ########## if the item "Link a Server" is selected ##########
			case R.id.linkServer :

				final ArrayList<Server> servers = db.getServerList();
				final Profile profileToLink = db.getProfileByName(nameSelectedItem);

				// This condition is specified to avoid the fact that 
				// the list becomes longer by multiplying the click on the item
				if(item.getSubMenu().size() < servers.size()){
					for(int j = 0 ; j < servers.size() ; j++){

						// We add the server's name to the subMenu and allow it to be checked
						item.getSubMenu().add(servers.get(j).getName());
						item.getSubMenu().getItem(j).setCheckable(true);

						// if the link already exists, we check the box
						if(db.linkAlreadyExisting(profileToLink, servers.get(j))){
							item.getSubMenu().getItem(j).setChecked(true);
						}

						// if the server is already linked to another profile, we cannot link it
						if(db.serverLinkedToAnotherProfile(servers.get(j), profileToLink)){
							item.getSubMenu().getItem(j).setCheckable(false);
							item.getSubMenu().getItem(j).setEnabled(false);
						}

						item.getSubMenu().getItem(j).setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								Database database = new Database(c);
								Server serverToLink = database.getServerByName(item.getTitle().toString());

								if(!item.isChecked()){
									// Add a link
									database.addLinkProfileServer(profileToLink, serverToLink);
									item.setChecked(true);
									Toast.makeText(c,serverToLink.getName() + " is now link to the profile " + profileToLink.getProfile_name(), Toast.LENGTH_LONG).show();
								}
								else{
									// Delete the existing link
									database.deleteLinkProfileServer(profileToLink, serverToLink);
									item.setChecked(false);
								}

								database.close();
								return true;
							}
						});
					}

					// Now, the subMenu is created
					subMenuLinkCreated = true;
				}

				db.close(); // close the database
				return true;

			default:
				db.close();
				mode.finish(); // the actionMode disappears
				return false;
			}
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// the oldView & the currentView loose the "focus"
			oldView.setBackgroundColor(profiles_LV.getCacheColorHint());
			currentView.setBackgroundColor(profiles_LV.getCacheColorHint());
			// the actionMode is destroyed by putting it at null
			mActionMode = null;
		}
	}


	/**
	 * 
	 * This method allows to checking link between all available servers and the selected profile
	 * If a link is detected, the server is checked in the subMenu's "Link a Server" item
	 * 
	 * @param menu
	 */

	public void checkingLinkBetweenProfileAndServers(Menu menu){

		Database db = new Database(c);
		ArrayList<Server> servers = db.getServerList();

		if(servers.size() != 0){	
			for(int s = 0 ; s < servers.size() ; s++){
				if(db.linkAlreadyExisting(db.getProfileByName(nameSelectedItem), servers.get(s))){
					menu.getItem(3).getSubMenu().getItem(s).setChecked(true);
				}
			}
		}

		db.close();
	}

	/**
	 * 
	 * This method is called each time the system goes back on this activity
	 * 
	 */

	@ Override
	protected void onResume(){
		super.onResume();
		// Use a Bundle to collect the new name of the profile
		Bundle bundleEdit = this.getIntent().getBundleExtra("NewValue");
		Bundle bundleAdd = this.getIntent().getBundleExtra("NewProfile");

		if(bundleEdit != null && bundleEdit.containsKey("NewValue")){
			String nameProfile = bundleEdit.getString("NewNameProfile");
			this.profiles_AL.get(indexSelectedItem).setProfile_name(nameProfile);

		}

		if(bundleAdd != null && bundleAdd.containsKey("NewProfile")){

			// We collect the new profile
			Profile newP;
			newP = new Profile(
					bundleAdd.getString("NameProfile"),
					bundleAdd.getString("FirstNickProfile"), 
					bundleAdd.getString("SecondNickProfile"),
					bundleAdd.getString("ThirdNickProfile"),
					bundleAdd.getString("UserNameProfile"),
					bundleAdd.getString("RealNameProfile")
					);			
			// We add the new profile to the list
			this.profiles_AL.add(newP);
		}

		// Update the list and its display
		this.adapterProfile.notifyDataSetChanged();
		this.adapterProfile.notifyDataSetInvalidated();

		this.actionBar.setTitle("Profiles  (" + this.profiles_AL.size() + ")");
	}

	/**
	 * Define the display of the actionBar
	 * 
	 * @param menu
	 * @return true
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.existing_object_activity, menu);
		return true;
	}

	/**
	 * 
	 * This method allows you to configure the behavior of the icon 
	 * in the action bar: When this button is clicked, the current activity 
	 * is removed and the previous activity becomes the current activity
	 * 
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			intent = new Intent(this, MenuActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;

		case R.id.add :
			intent = new Intent(ExistingProfilesActivity.this, CreateProfileActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bundle = new Bundle();
			bundle.putBoolean("comingFromExistingProfilesActivity", true);
			intent.putExtra("AddingFromExistingProfilesActivity", bundle);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	/**
	 * 
	 * This method allows you to configure the behavior of the physical button 
	 * "Back" (on device) : When this button is pushed, the current activity 
	 * is removed and the previous activity becomes the current activity
	 * 
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			Intent intent = new Intent(this, MenuActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}