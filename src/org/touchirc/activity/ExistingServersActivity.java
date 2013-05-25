package org.touchirc.activity;

import java.util.ArrayList;

import org.touchirc.R;
import org.touchirc.R.layout;
import org.touchirc.adapter.ServerAdapter;
import org.touchirc.db.Database;
import org.touchirc.model.Server;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ExistingServersActivity extends SherlockListActivity {

	private ListView servers_LV;
	private ArrayList<Server> servers_AL;
	private ServerAdapter adapterServer;
	private int indexSelectedItem; // selected server's index in the listView
	private String nameSelectedItem; // selected server's name
	private Context c;
	private ActionBar actionBar;

	protected ActionMode mActionMode; // Variable used for triggering the actionMode (ActionBar)
	private static View oldView; // Variable used to store the old view selected
	private static View currentView; // Variable used to store the current view selected

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Allows the icon to do "Previous"
		this.actionBar = getSupportActionBar();
		this.actionBar.setDisplayHomeAsUpEnabled(true);

		setContentView(layout.listview_layout);

		// Collect the context
		c = this;

		// Collect the server widgets ListView (LV)
		this.servers_LV = (ListView) findViewById(android.R.id.list);

		// the LV is always focused on its last item
		this.servers_LV.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		// Collect the Servers list
		this.servers_AL = new Database(getApplicationContext()).getServerList();

		this.actionBar.setTitle("Servers  (" + this.servers_AL.size() + ")");

		// Put an ArrayAdapter so that the LV and the servers list be linked
		this.adapterServer =  new ServerAdapter(servers_AL, c);
		this.setListAdapter(adapterServer);

		/**
		 * 
		 * When the user click on an item an ActionMode Bar appears.
		 * It provides him some features on the selected server : Set auto-connect this server, Edit, Delete.
		 * 
		 */

		servers_LV.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long arg3) {

				indexSelectedItem = position;
				currentView = v;

				// if the ActionMode is already displayed
				if (mActionMode != null) {

					oldView.setBackgroundColor(servers_LV.getCacheColorHint());
					mActionMode.finish(); // closing it
					mActionMode = startActionMode(new ActionModeServerSettings()); // and launching it to update values
					v.setBackgroundColor(Color.GRAY); // the selected item in the listView is highlighted
					oldView = v;
				}
				else{

					// Start the CallBackActionBar using the ActionMode.Callback defined below
					mActionMode = startActionMode(new ActionModeServerSettings()); // launching the ActionMode
					oldView = v;
					v.setSelected(true);
					v.setBackgroundColor(Color.GRAY); // the selected item in the listView is highlighted
				}
				return false;
			}
		});
	}

	/**
	 * 
	 * Here, we define the behavior of the actionMode according the input events
	 * thanks to a specific class
	 * 
	 */

	private final class ActionModeServerSettings implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			MenuInflater inflater = getSupportMenuInflater();

			// Load the contextual Menu
			inflater.inflate(R.menu.context_menu_server, menu);

			// Put all the server's name in an Array, in order to ...
			String [] servers = new String [servers_AL.size()];
			for(int s = 0 ; s < servers_AL.size() ; s++){
				servers[s] = servers_AL.get(s).getName();
			}

			// ... collect the name of the selected server
			nameSelectedItem = servers[indexSelectedItem];

			// if the server is already the auto-connected one, we cannot set it
			if(nameSelectedItem.equals(new Database(c).nameAutoConnectedServer())){
				menu.getItem(2).setTitle(R.string.AUTO);
				menu.getItem(2).setEnabled(false);
			}
			else{
				menu.getItem(2).setTitle(R.string.AUTO);;
			} 

			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // if nothing is done
		}

		// Called when the user selects a contextual menu item
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			Database db = new Database(c);

			switch (item.getItemId()) {		

			// ########## if the item "AutoConnect" is selected ##########
			case R.id.autoConnect :

				// The selected server is now the auto-connected one
				if(db.setAutoConnect(nameSelectedItem)){
					mode.getMenu().getItem(2).setTitle(R.string.AUTO);
					mode.getMenu().getItem(2).setEnabled(false);
					Toast.makeText(c, nameSelectedItem + " is now used for autoconnection !", Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(c, "An error occurred while setting the server :(", Toast.LENGTH_LONG).show();
				}

				// Notifying the adapter to update the display
				adapterServer.notifyDataSetInvalidated();

				db.close(); // close the database
				return true;

			// ########## if the item "Edit" is selected ##########		
			case R.id.edit : 

				// Collect all the informations concerning the current server
				Server serverToEdit = db.getServerByName(nameSelectedItem);

				// Prepare the Intent to switch on the activity which allows to modify the server
				Intent i = new Intent(ExistingServersActivity.this, CreateServerActivity.class);

				// Put the informations of the current server into a Bundle object
				Bundle b = new Bundle();
				b.putString("ServerName", serverToEdit.getName());
				b.putString("HostName", serverToEdit.getHost());
				b.putInt("portNumber", serverToEdit.getPort());
				b.putString("ServerPassword", serverToEdit.getPassword());

				// Assign the Bundle to the Intent
				i.putExtra("ServerIdentification", b);

				// Start the Intent
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);

				finish(); // close the current activity

				db.close(); // close the database
				return true;

			// ########## if the item "Delete" is selected ##########
			case R.id.delete :

				// Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(ExistingServersActivity.this);

				// Chain together various setter methods to set the dialog characteristics
				builder.setTitle(R.string.deleteServer)
				.setMessage("Would you really like to delete the server : " + nameSelectedItem + " ?")
				.setIcon(android.R.drawable.ic_menu_delete);

				final ActionMode am = mode;

				builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
						// Collect the auto-connected server before removal of the selected server
						String autoconnectedServer = new Database(c).nameAutoConnectedServer();

						// Removal throughout the db
						if (new Database(c).deleteServer(nameSelectedItem)){ // if the deletion is successful
							// Remove the corresponding server from the list
							servers_AL.remove(indexSelectedItem);
							// Notify the adapter that the list's state has changed
							adapterServer.notifyDataSetChanged();
							// Update the number of available servers in the TV
							actionBar.setTitle("Servers  (" + servers_AL.size() + ")");
						}
						
						/** ------------------------------------------------------- **/
						
						// the selected server is the one used for auto-connection
						if(nameSelectedItem.equals(autoconnectedServer)){
							
						}
						
						// Instantiate an AlertDialog.Builder with its constructor
						AlertDialog.Builder builderAutoconnect = new AlertDialog.Builder(ExistingServersActivity.this);

						// Chain together various setter methods to set the dialog characteristics
						builderAutoconnect.setTitle(R.string.warning)
						.setMessage("A new autoconnected server has to be setted.\nChoose one, thanks you :");

						builderAutoconnect.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// TODO Suggest a  list of servers to select the new one used for auto-connection
							}
						});
						builderAutoconnect.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

						// Get the AlertDialog from create()
						AlertDialog dialogAutoconnect = builderAutoconnect.create();
						dialogAutoconnect.show();
						
						/** ------------------------------------------------------- **/
						
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

			default:
				db.close();
				mode.finish(); // the actionMode disappears
				return false;
			}
		}

		// Called when the user exits the action mode
		public void onDestroyActionMode(ActionMode mode) {
			// the oldView & currentView loose the "focus"
			oldView.setBackgroundColor(servers_LV.getCacheColorHint());
			currentView.setBackgroundColor(servers_LV.getCacheColorHint());
			// the actionMode is destroyed by putting it at null
			mActionMode = null;
		}
	}

	// TODO When the user simple click on a server of the list, it connects the user to this server
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		if(mActionMode == null){
			Log.i("TouchIRC ", "Attempt to connect to the server " + this.servers_AL.get(position) +" !");
		}
	}

	/**
	 * 
	 * This method is called each time the system goes back on this activity
	 * 
	 */

	@ Override
	protected void onResume(){
		super.onResume();
		// Use a Bundle to collect the new name of the server or the new Server
		Bundle bundleEdit = this.getIntent().getBundleExtra("NewValue");
		Bundle bundleAdd = this.getIntent().getBundleExtra("NewServer");

		// We come from CreateServerActivity and the server's name has changed
		if(bundleEdit != null && bundleEdit.containsKey("NewValue")){
			String nameServer = bundleEdit.getString("NewNameServer");
			this.servers_AL.get(indexSelectedItem).setName(nameServer);
		}

		// We come from CreateServerActivity and a new server has been added
		if(bundleAdd != null && bundleAdd.containsKey("NewServer")){

			// We collect the new server
			Server newS;

			// According the presence of a password
			if(!bundleAdd.containsKey("PasswordServer")){
				newS = new Server(	bundleAdd.getString("NameServer"), 
						bundleAdd.getString("HostnameServer"),
						bundleAdd.getInt("PortServer")
						);
			}
			else{
				newS = new Server(	bundleAdd.getString("NameServer"), 
						bundleAdd.getString("HostnameServer"),
						bundleAdd.getInt("PortServer"),
						bundleAdd.getString("PasswordServer")
						);	
			}

			// We add the new server to the list
			this.servers_AL.add(newS);
		}

		// Update the list and its display
		this.adapterServer.notifyDataSetChanged();
		this.adapterServer.notifyDataSetInvalidated();

		this.actionBar.setTitle("Servers  (" + this.servers_AL.size() + ")");
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
			intent = new Intent(ExistingServersActivity.this, CreateServerActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bundle = new Bundle();
			bundle.putBoolean("comingFromExistingServersActivity", true);
			intent.putExtra("AddingFromExistingServersActivity", bundle);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 
	 * This method allows you to configure the behavior of the physical button 
	 * "Back" (on device) : When this button is clicked, the current activity 
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
