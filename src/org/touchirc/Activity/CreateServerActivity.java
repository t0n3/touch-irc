package org.touchirc.activity;

import org.touchirc.R;

import org.touchirc.db.Database;
import org.touchirc.model.Server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class CreateServerActivity extends Activity{

	private TextView server_Name_TV;
	private EditText serverName_ET;

	private TextView server_Hostname_TV;
	private EditText serverHostname_ET;

	private TextView server_port_TV;
	private EditText serverPort_ET;

	private TextView server_password_TV;
	private EditText serverPassword_ET;

	private Server serv;
	private Bundle b = null;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.create_server_layout);

		Intent i = getIntent();

		// Collect the Bundle object if the activity is started by ExistingServersActivity
		b = i.getBundleExtra("ServerIdentification");

		// TextViews : black ones (default color)s are compulsories, gray ones are optionals

		this.server_Name_TV = (TextView) findViewById(R.id.textView_server_name);	

		this.server_Hostname_TV = (TextView) findViewById(R.id.textView_hostname);

		this.server_port_TV = (TextView) findViewById(R.id.textView_server_port);

		this.server_password_TV = (TextView) findViewById(R.id.textView_server_password);
		this.server_password_TV.setTextColor(Color.GRAY);

		// EditTexts		

		this.serverName_ET = (EditText) findViewById(R.id.editText_server_name);
		// if started by ExistingServersActivity, changing the EditText
		if(b != null && b.containsKey("ServerName")){
			this.serverName_ET.setText(b.getString("ServerName"));
		}

		this.serverHostname_ET = (EditText) findViewById(R.id.editText_hostname);
		// if started by ExistingServersActivity, changing the EditText
		if(b != null && b.containsKey("HostName")){
			this.serverHostname_ET.setText(b.getString("HostName"));
		}

		this.serverPort_ET = (EditText) findViewById(R.id.editText_server_port);
		// if started by ExistingServersActivity, changing the EditText
		if(b != null && b.containsKey("portNumber")){
			this.serverPort_ET.setText(String.valueOf(b.getInt("portNumber")));
		}

		this.serverPassword_ET = (EditText) findViewById(R.id.editText_server_password);
		// if started by ExistingServersActivity, changing the EditText
		if(b != null && b.containsKey("ServerPassword") && b.getString("ServerPassword").length() > 0){
			this.server_password_TV.setTextColor(Color.BLACK); // To highlight the fact that a password exists
			this.serverPassword_ET.setText(b.getString("ServerPassword"));
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
					
					if(b != null){
						// Update the Server in the database
						db.updateServer(serv, b.getString("ServerName"));
						Toast.makeText(getApplicationContext(), "The server : " + serv.getName() + " has been modified !", Toast.LENGTH_SHORT).show();
						
						Intent i = new Intent(CreateServerActivity.this, ExistingServersActivity.class);
						// Use a Bundle to transfer the Server modified
						Bundle b = new Bundle();
						b.putString("NewNameServer", serv.getName());
						i.putExtra("NewValue", b);
						startActivity(i);
					}
					else{
						// Add the Server just created into the database
						db.addServer(serv);
						Toast.makeText(getApplicationContext(), "The server : " + serv.getName() + " has been added !", Toast.LENGTH_SHORT).show();
					}
					
					db.close();
					
					handled = true;					
					finish(); // close the activity
				}
				else{
					// We alarm the user of missing informations
					Toast.makeText(getApplicationContext(), "Missing informations ...", Toast.LENGTH_SHORT).show();

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
}