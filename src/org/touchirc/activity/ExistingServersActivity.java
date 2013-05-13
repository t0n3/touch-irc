package org.touchirc.activity;

import java.util.ArrayList;

import org.touchirc.R;
import org.touchirc.R.layout;
import org.touchirc.adapter.ServerAdapter;
import org.touchirc.db.Database;
import org.touchirc.model.Server;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExistingServersActivity extends ListActivity{

	private TextView servers_TV;
	private ListView servers_LV;
	private ArrayList<Server> servers_AL;
	private ServerAdapter adapterServer;
	private int indexSelectedItem; // selected server's index in the listView
	private String nameSelectedItem; // selected server's name
	private Context c;

	protected Object mActionMode; // Variable used for triggering the actionMode (ActionBar)
	private static View oldView; // Variable used to store the old view selected

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(layout.listview_layout);

		// Collect the context
		c = this;

		// Collect the server widgets TextView (TV), ListView (LV)
		this.servers_TV = (TextView) findViewById(R.id.TV_Objects_name);
		this.servers_LV = (ListView) findViewById(android.R.id.list);

		// the LV is always focused on its last item
		this.servers_LV.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		// Collect the Servers list
		this.servers_AL = new Database(getApplicationContext()).getServerList();

		servers_TV.setText("Servers List :       (" + this.servers_AL.size() + ")");

		// Put an ArrayAdapter so that the LV and the servers list be linked
		this.adapterServer =  new ServerAdapter(servers_AL, c);
		this.setListAdapter(adapterServer);

		/**
		 * 
		 * When the user longclick on an item an ActionMode Bar appears.
		 * It provides him some features on the selected server : Set autoconnect this server, Edit, Delete.
		 * 
		 */

		servers_LV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int position, long arg3) {

				indexSelectedItem = position;

				// if the ActionMode is already displayed
				if (mActionMode != null) {
					ExistingServersActivity.this.mActionModeCallback.onDestroyActionMode((ActionMode)mActionMode); // closing it
					mActionMode = ExistingServersActivity.this.startActionMode(mActionModeCallback); // and launching it to update values

					v.setBackgroundColor(Color.GRAY); // the selected item in the listView is highlighted
					oldView = v;

					return false;
				}				

				// Start the CallBackActionBar using the ActionMode.Callback defined below
				mActionMode = ExistingServersActivity.this.startActionMode(mActionModeCallback); // launching the ActionMode
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
			inflater.inflate(R.menu.context_menu_server, menu);

			// Put all the server's name in an Array, in order to ...
			String [] servers = new String [servers_AL.size()];
			for(int s = 0 ; s < servers_AL.size() ; s++){
				servers[s] = servers_AL.get(s).getName();
			}

			// ... collect the name of the selected server
			nameSelectedItem = servers[indexSelectedItem];

			// if the server is already the autoconnected one, we cannot set it
			// the icon is updated
			if(nameSelectedItem.equals(new Database(c).nameAutoConnectedServer())){
				menu.getItem(0).setIcon(android.R.drawable.star_on);
				menu.getItem(0).setEnabled(false);
			}
			else{
				menu.getItem(0).setIcon(android.R.drawable.star_off);
			} 

			return true;
		}

		// Called each time the action mode is shown. Always called after
		// onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // if nothing is done
		}

		// Called when the user selects a contextual menu item
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			Database db = new Database(c);

			switch (item.getItemId()) {		

			// ########## if the item "AutoConnect" is selected ##########
			case R.id.autoConnect :

				// The selected server is now the autoconnected one
				if(db.setAutoConnect(nameSelectedItem)){
					mode.getMenu().getItem(0).setIcon(android.R.drawable.star_on);
					mode.getMenu().getItem(0).setEnabled(false);
					Toast.makeText(c, nameSelectedItem + " is now used for autoconnecting !", Toast.LENGTH_LONG).show();
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

				mode.finish(); // the actionMode disappears

				// Removal throughout the db
				if (db.deleteServer(nameSelectedItem)){ // if the deletion is successful
					// Notify the user thanks to a Toast
					Toast.makeText(c, "Server " + nameSelectedItem + " deleted.", Toast.LENGTH_LONG).show();
					// Remove the corresponding server from the list
					servers_AL.remove(indexSelectedItem);
					// Notify the adapter that the list's state has changed
					adapterServer.notifyDataSetChanged();
					// Update the number of available servers in the TV
					servers_TV.setText("Servers List :       (" + servers_AL.size() + ")");
				}
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
			oldView.setBackgroundColor(servers_LV.getCacheColorHint());
			// the actionMode is destroyed by putting it at null
			mActionMode = null;
		}
	};
	
	/**
	 * 
	 * This method is called each time the system goes back on this activity
	 * 
	 */

	@ Override
	protected void onResume(){
		super.onResume();
		// Use a Bundle to collect the new name of the server
		Bundle b = this.getIntent().getBundleExtra("NewValue");
		if(b != null && b.containsKey("NewValue")){
			String nameServer = b.getString("NewNameServer");
			this.servers_AL.get(indexSelectedItem).setName(nameServer);

			// Update the list and its display
			this.adapterServer.notifyDataSetChanged();
			this.adapterServer.notifyDataSetInvalidated();
		}
	}
}
