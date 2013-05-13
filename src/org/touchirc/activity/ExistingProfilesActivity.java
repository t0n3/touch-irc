package org.touchirc.activity;

import java.util.ArrayList;

import org.touchirc.R;
import org.touchirc.R.layout;
import org.touchirc.db.Database;
import org.touchirc.model.Profile;
import org.touchirc.model.ProfileAdapter;
import org.touchirc.model.Server;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExistingProfilesActivity extends ListActivity{

	private TextView profiles_TV;
	private ListView profiles_LV;
	private ArrayList<Profile> profiles_AL;
	private ProfileAdapter adapterProfile;
	private int indexSelectedItem; // selected profile's index in the listView
	private String nameSelectedItem; // selected profile's name
	private Context c;

	protected Object mActionMode; // Variable used for triggering the actionMode (ActionBar)
	private static View oldView; // Variable used to store the old view selected
	protected boolean subMenuLinkCreated = false; // Variable used to warn OnPrepare method first display ActionBar

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

		/**
		 * 
		 * When the user longclick on an item an ActionMode Bar appears.
		 * It provides him some features on the selected profile : Set by Default, Edit, Link a Server, Delete.
		 * 
		 */

		profiles_LV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int position, long arg3) {

				indexSelectedItem = position;

				// if the ActionMode is already displayed
				if (mActionMode != null) {
					ExistingProfilesActivity.this.mActionModeCallback.onDestroyActionMode((ActionMode)mActionMode); // closing it
					mActionMode = ExistingProfilesActivity.this.startActionMode(mActionModeCallback); // and launching it to update values

					v.setBackgroundColor(Color.GRAY); // the selected item in the listView is highlighted
					oldView = v;

					return false;
				}				

				// Start the CallBackActionBar using the ActionMode.Callback defined below
				mActionMode = ExistingProfilesActivity.this.startActionMode(mActionModeCallback); // launching the ActionMode
				oldView = v;
				v.setSelected(true);
				v.setBackgroundColor(Color.GRAY); // the selected item in the listView is highlighted
				return true;
			}
		});
	}

	/**
	 * 
	 * Here, we define the behavior of the actionMode according the input events
	 * 
	 */

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = getMenuInflater();

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
				menu.getItem(0).setIcon(android.R.drawable.star_on);
				menu.getItem(0).setEnabled(false);
			}
			else{
				menu.getItem(0).setIcon(android.R.drawable.star_off);
			}

			// the subMenu providing the availables servers is not still created
			subMenuLinkCreated = false; 

			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// if the subMenu is created, we can check the links between profiles & servers
			if(subMenuLinkCreated){
				checkingLinkBetweenProfileAndServers(menu);
			}
			return true;
		}

		// Called when the user selects a contextual menu item
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			Database db = new Database(c);

			switch (item.getItemId()) {		

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
						else{
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
					}

					// Now, the subMenu is created
					subMenuLinkCreated = true;
				}

				db.close(); // close the database
				return true;

				// ########## if the item "Set By Default" is selected ##########
			case R.id.setByDefault :

				// The selected profile is now the default profile
				if(db.setDefaultProfile(nameSelectedItem)){
					mode.getMenu().getItem(0).setIcon(android.R.drawable.star_on);
					mode.getMenu().getItem(0).setEnabled(false);
					Toast.makeText(c, nameSelectedItem + " is now the profile by default !", Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(c, "An error occurred while setting the profile by default :(", Toast.LENGTH_LONG).show();
				}

				// Notifying the adapter to update the display
				adapterProfile.notifyDataSetInvalidated();

				db.close(); // close the database
				return true;

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

				mode.finish(); // the actionMode disappears

				// Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(ExistingProfilesActivity.this);

				// Chain together various setter methods to set the dialog characteristics
				builder.setTitle(R.string.deleteProfile)
				.setMessage("Would you really like to delete the profile : " + nameSelectedItem + " ?")
				.setIcon(android.R.drawable.ic_menu_delete);

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
							// Update the number of available profiles in the TV
							profiles_TV.setText("Profiles List :       (" + profiles_AL.size() + ")");
						}
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

			default:
				db.close();
				mode.finish(); // the actionMode disappears
				return false;
			}
		}

		// Called when the user exits the action mode
		public void onDestroyActionMode(ActionMode mode) {
			// the oldView looses the "focus"
			oldView.setBackgroundColor(profiles_LV.getCacheColorHint());
			// the actionMode is destroyed by putting it at null
			mActionMode = null;
		}
	};

	/**
	 * 
	 * This method allows to checking link between all availables servers and the selected profile
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
		Bundle b = this.getIntent().getBundleExtra("NewValue");
		if(b != null && b.containsKey("NewValue")){
			String nameProfile = b.getString("NewNameProfile");
			this.profiles_AL.get(indexSelectedItem).setProfile_name(nameProfile);

			// Update the list and its display
			this.adapterProfile.notifyDataSetChanged();
			this.adapterProfile.notifyDataSetInvalidated();			
		}
	}
}
