package org.touchirc.activity;

import java.util.ArrayList;

import org.touchirc.R;
import org.touchirc.R.layout;
import org.touchirc.db.Database;
import org.touchirc.model.Server;
import org.touchirc.model.ServerAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExistingServersActivity extends ListActivity{

	private TextView servers_TV;
	private ListView servers_LV;
	private ArrayList<Server> servers_AL;
	private ServerAdapter adapterServer;
	private int index;
	private Context c;

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

		registerForContextMenu(servers_LV);
	}

	/**
	 * When the user long click on a View of the ListView, he triggers
	 * a ContextMenu which suggest him "AutoConnecting", Editing or Removing the current server
	 */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		// Load the context Menu
		inflater.inflate(R.menu.context_menu_server, menu);
		// Give a title to the ContextMenu
		menu.setHeaderTitle("Options server");
		
		// Collecting the name of the selected server, using a AdapterContextMenuInfo
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

		// Put all the server's name in an Array
		String [] servers = new String [servers_AL.size()];
		for(int s = 0 ; s < servers_AL.size() ; s++){
			servers[s] = servers_AL.get(s).getName();
		}

		// Collecting the name of the selected server
		String selectedServer_name = servers[(int)info.id];
		
		// if the server is already used for auto-connection, we cannot use it for auto-connection
		if(selectedServer_name.equals(new Database(c).nameAutoConnectedServer())){
			menu.getItem(0).setEnabled(false);
		}
	}

	/**
	 * According the Item selected ("Use for Auto-Connection", "Edit", "Delete")
	 * specific statements will be done
	 * 
	 */

	public boolean onContextItemSelected(MenuItem item) {

		// Collecting the name of the selected server, using a AdapterContextMenuInfo
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		// Put all the server's name in an Array
		String [] servers = new String [servers_AL.size()];
		for(int s = 0 ; s < servers_AL.size() ; s++){
			servers[s] = servers_AL.get(s).getName();
		}

		// Collecting the name and position of the selected server
		String selectedServer_name = servers[(int)info.id];
		int selectedServer_position = (int)info.id;
		this.index = selectedServer_position;

		// if the item "Use for Auto-Connection" is selected
		if(item.getItemId() == R.id.autoConnect){

			Database db = new Database(c);
			// The selected server is now used to auto-connect
			if(db.setAutoConnect(selectedServer_name)){
				Toast.makeText(c, selectedServer_name + " is now used to auto-connect !", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(c, "An error occurred while setting the server :(", Toast.LENGTH_LONG).show();
			}

			db.close();

			// Notifying the adapter to update the display
			adapterServer.notifyDataSetInvalidated();

			return true;
		}

		// if the item "Edit" is selected		
		if(item.getItemId() == R.id.edit){

			// Collect all the informations concerning the current server
			Database db = new Database(c);
			Server serverToEdit = db.getServerByName(selectedServer_name);

			// Prepare the Intent to switch on the activity which allows to modify the server
			Intent i = new Intent(this, CreateServerActivity.class);

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

			db.close();
			
			finish();

			return true;
		}

		// if the item "Delete" is selected
		if(item.getItemId() == R.id.delete){

			// Removal throughout the db
			Database db = new Database(c);

			if (db.deleteServer(selectedServer_name)){ // if the deletion is successful
				// Notify the user thanks to a Toast
				Toast.makeText(c, "Server " + selectedServer_name + " deleted.", Toast.LENGTH_LONG).show();
				db.close();
				// Remove the corresponding server from the list
				servers_AL.remove(selectedServer_position);
				// Notify the adapter that the list's state has changed
				adapterServer.notifyDataSetChanged();
				// Update the number of available servers in the TV
				servers_TV.setText("Profiles List :       (" + this.servers_AL.size() + ")");
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
			String nameServer = b.getString("NewNameServer");
			this.servers_AL.get(index).setName(nameServer);

			// Update the list and its display
			this.adapterServer.notifyDataSetChanged();
			this.adapterServer.notifyDataSetInvalidated();
		}
	}
}
