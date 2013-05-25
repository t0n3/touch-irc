package org.touchirc.activity;

import org.touchirc.R;

import org.touchirc.db.Database;
import org.touchirc.model.Server;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class CreateServerActivity extends SherlockActivity {

	private TextView server_Name_TV;
	private EditText serverName_ET;

	private TextView server_Hostname_TV;
	private EditText serverHostname_ET;

	private TextView server_port_TV;
	private EditText serverPort_ET;

	private TextView server_password_TV;
	private EditText serverPassword_ET;

	private Server serv;
	private Bundle bundleEdit = null;
	private Bundle bundleAddFromMenu = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Allows to the actionBar's icon to do "Previous"
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Create a Server");

		// Prevents the keyboard to be displayed when you come on this activity
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.create_server_layout);

		Intent i = getIntent();

		// Collect the Bundle object if the activity is started by ExistingServersActivity : Add or Edit
		bundleEdit = i.getBundleExtra("ServerIdentification");
		bundleAddFromMenu = i.getBundleExtra("AddingFromExistingServersActivity");

		// TextViews : black ones (default color)s are compulsories, gray ones are optionals

		this.server_Name_TV = (TextView) findViewById(R.id.textView_server_name);	

		this.server_Hostname_TV = (TextView) findViewById(R.id.textView_hostname);

		this.server_port_TV = (TextView) findViewById(R.id.textView_server_port);

		this.server_password_TV = (TextView) findViewById(R.id.textView_server_password);
		this.server_password_TV.setTextColor(Color.GRAY);

		// EditTexts		

		this.serverName_ET = (EditText) findViewById(R.id.editText_server_name);
		// if started by ExistingServersActivity, changing the EditText
		if(bundleEdit != null && bundleEdit.containsKey("ServerName")){
			this.serverName_ET.setText(bundleEdit.getString("ServerName"));
		}

		this.serverHostname_ET = (EditText) findViewById(R.id.editText_hostname);
		// if started by ExistingServersActivity, changing the EditText
		if(bundleEdit != null && bundleEdit.containsKey("HostName")){
			this.serverHostname_ET.setText(bundleEdit.getString("HostName"));
		}

		this.serverPort_ET = (EditText) findViewById(R.id.editText_server_port);
		// if started by ExistingServersActivity, changing the EditText
		if(bundleEdit != null && bundleEdit.containsKey("portNumber")){
			this.serverPort_ET.setText(String.valueOf(bundleEdit.getInt("portNumber")));
		}

		this.serverPassword_ET = (EditText) findViewById(R.id.editText_server_password);
		// if started by ExistingServersActivity, changing the EditText
		if(bundleEdit != null && bundleEdit.containsKey("ServerPassword")){
			this.server_password_TV.setTextColor(Color.BLACK); // To highlight the fact that a password exists
			this.serverPassword_ET.setText(bundleEdit.getString("ServerPassword"));
		}

		/**
		 * Create an OnEditorActionListener Object :
		 * When the Key "Done" is pressed on the server_password EditText : a Server is created.
		 */

		this.serverPassword_ET.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				boolean handled = false;

				// the keyboard disappears
				InputMethodManager mngr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mngr.hideSoftInputFromWindow(serverPassword_ET.getWindowToken(), 0);

				if (arg1 == EditorInfo.IME_ACTION_DONE && (
						serverName_ET.getText().length() != 0 && 
						serverHostname_ET.getText().length() != 0 &&
						serverPort_ET.getText().length() != 0)) {

					// the Server is created with the datas given by the user
					int port = Integer.parseInt(serverPort_ET.getText().toString());

					serv = new Server(serverName_ET.getText().toString(),
							serverHostname_ET.getText().toString(),
							port,
							serverPassword_ET.getText().toString()
							);

					Database db = new Database(getApplicationContext());
					Intent i;
					Bundle b;

					if(bundleEdit != null){
						// Update the Server in the database
						db.updateServer(serv, bundleEdit.getString("ServerName"));
						Toast.makeText(getApplicationContext(), "The server : " + serv.getName() + " has been modified !", Toast.LENGTH_SHORT).show();

						i = new Intent(CreateServerActivity.this, ExistingServersActivity.class);
						// Use a Bundle to transfer the Server modified
						b = new Bundle();
						b.putString("NewNameServer", serv.getName());
						i.putExtra("NewValue", b);
						startActivity(i);
					}
					else{
						// Add the Server just created into the database
						db.addServer(serv);
						Toast.makeText(getApplicationContext(), "The server : " + serv.getName() + " has been added !", Toast.LENGTH_SHORT).show();
						
						// We go back to the ExistingServersActivity and transmit the new server
						if(bundleAddFromMenu != null && bundleAddFromMenu.containsKey("comingFromExistingServersActivity")){
							i = new Intent(CreateServerActivity.this, ExistingServersActivity.class);
							// Use a Bundle to transfer the Server created
							b = new Bundle();
							b.putString("NameServer", serv.getName());
							b.putString("HostnameServer", serv.getHost());
							b.putInt("PortServer", serv.getPort());
							if(serv.isProtected()){
								b.putString("PasswordServer", serv.getPassword());
							}
							i.putExtra("NewServer", b);
							startActivity(i);
						}
					}

					db.close();

					handled = true;					
					finish(); // close the activity
				}
				else{
					// We alarm the user of missing informations
					Toast.makeText(getApplicationContext(), "Missing/Invalid informations ...", Toast.LENGTH_SHORT).show();

					// And indicate him of which informations we are lacking of (the color of the corresponding textviews becomes red)
					if(serverPort_ET.getText().length() == 0){
						server_port_TV.setTextColor(Color.RED);
						server_port_TV.invalidate();
						serverPort_ET.requestFocus();
					}
					if(serverHostname_ET.getText().length() == 0){
						server_Hostname_TV.setTextColor(Color.RED);
						server_Hostname_TV.invalidate();
						serverHostname_ET.requestFocus();
					}
					if(serverName_ET.getText().length() == 0){
						server_Name_TV.setTextColor(Color.RED);
						server_Name_TV.invalidate();
						serverName_ET.requestFocus();
					}
				}
				return handled;
			}
		});
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
		inflater.inflate(R.menu.create_object_activity, menu);
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
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, MenuActivity.class);
            // According to the origin of the triggering of the activity
            if(bundleEdit != null || bundleAddFromMenu != null){
	            intent.setClass(this, ExistingServersActivity.class);
            }
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		
		case R.id.save :
			
			if (serverName_ET.getText().length() != 0 && 
				serverHostname_ET.getText().length() != 0 &&
				serverPort_ET.getText().length() != 0) {

				// the Server is created with the datas given by the user
				int port = Integer.parseInt(serverPort_ET.getText().toString());

				serv = new Server(	serverName_ET.getText().toString(),
									serverHostname_ET.getText().toString(),
									port,
									serverPassword_ET.getText().toString()
								);

				Database db = new Database(getApplicationContext());
				Intent i;
				Bundle b;

				if(bundleEdit != null){
					// Update the Server in the database
					db.updateServer(serv, bundleEdit.getString("ServerName"));
					Toast.makeText(getApplicationContext(), "The server : " + serv.getName() + " has been modified !", Toast.LENGTH_SHORT).show();

					i = new Intent(CreateServerActivity.this, ExistingServersActivity.class);
					// Use a Bundle to transfer the Server modified
					b = new Bundle();
					b.putString("NewNameServer", serv.getName());
					i.putExtra("NewValue", b);
					startActivity(i);
				}
				else{
					// Add the Server just created into the database
					db.addServer(serv);
					Toast.makeText(getApplicationContext(), "The server : " + serv.getName() + " has been added !", Toast.LENGTH_SHORT).show();
					
					// We go back to the ExistingServersActivity and transmit the new server
					if(bundleAddFromMenu != null && bundleAddFromMenu.containsKey("comingFromExistingServersActivity")){
						i = new Intent(CreateServerActivity.this, ExistingServersActivity.class);
						// Use a Bundle to transfer the Server created
						b = new Bundle();
						b.putString("NameServer", serv.getName());
						b.putString("HostnameServer", serv.getHost());
						b.putInt("PortServer", serv.getPort());
						if(serv.isProtected()){
							b.putString("PasswordServer", serv.getPassword());
						}
						i.putExtra("NewServer", b);
						startActivity(i);
					}
				}

				db.close();
			
				finish(); // close the activity
			}
			else{
				// We alarm the user of missing informations
				Toast.makeText(getApplicationContext(), "Missing/Invalid informations ...", Toast.LENGTH_SHORT).show();

				// And indicate him of which informations we are lacking of (the color of the corresponding textviews becomes red)
				if(serverPort_ET.getText().length() == 0){
					server_port_TV.setTextColor(Color.RED);
					server_port_TV.invalidate();
					serverPort_ET.requestFocus();
				}
				if(serverHostname_ET.getText().length() == 0){
					server_Hostname_TV.setTextColor(Color.RED);
					server_Hostname_TV.invalidate();
					serverHostname_ET.requestFocus();
				}
				if(serverName_ET.getText().length() == 0){
					server_Name_TV.setTextColor(Color.RED);
					server_Name_TV.invalidate();
					serverName_ET.requestFocus();
				}
			}
			
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
            // According to the origin of the triggering of the activity
            if(bundleEdit != null || bundleAddFromMenu != null){
	            intent.setClass(this, ExistingServersActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}