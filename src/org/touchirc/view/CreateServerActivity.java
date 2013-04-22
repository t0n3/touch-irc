package org.touchirc.view;

import org.touchirc.R;

import org.touchirc.db.Database;
import org.touchirc.model.Server;

import android.app.Activity;
import android.content.Context;
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
	private EditText server_Name;

	private TextView server_Hostname_TV;
	private EditText server_Hostname;

	private TextView server_port_TV;
	private EditText server_port;

	private TextView server_password_TV;
	private EditText server_password;

	private Server createdServer;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.create_server_layout);

		// TextViews : black ones (default color)s are compulsories, gray ones are optionals

		this.server_Name_TV = (TextView) findViewById(R.id.TVServerName);	

		this.server_Hostname_TV = (TextView) findViewById(R.id.TVHostName);

		this.server_port_TV = (TextView) findViewById(R.id.TVServerPort);

		this.server_password_TV = (TextView) findViewById(R.id.TVServerPassword);
		this.server_password_TV.setTextColor(Color.GRAY);

		// EditTexts		

		this.server_Name = (EditText) findViewById(R.id.editText_server_name);

		this.server_Hostname = (EditText) findViewById(R.id.editText_host_name);

		this.server_port = (EditText) findViewById(R.id.editText_server_port);

		this.server_password = (EditText) findViewById(R.id.editText_server_password);

		/**
		 * Create an OnEditorActionListener Object :
		 * When the Key "Done" is pressed on the server_password EditText : a Server is created.
		 */

		this.server_password.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				boolean handled = false;

				// the keyboard disappears
				InputMethodManager mngr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mngr.hideSoftInputFromWindow(server_password.getWindowToken(), 0);

				if (arg1 == EditorInfo.IME_ACTION_DONE && (
						server_Name.getText().length() != 0 && 
						server_Hostname.getText().length() != 0 &&
						server_port.getText().length() != 0)) {

					// the Server is created with the datas given by the user
					int port = Integer.parseInt(server_port.getText().toString());

					createdServer = new Server(server_Name.getText().toString(),
							server_Hostname.getText().toString(),
							port,
							server_password.getText().toString()
							);

					// Add the Server just created into the database
					Database db = new Database(getApplicationContext());
					db.addServer(createdServer);

					handled = true;

					Toast.makeText(getApplicationContext(), "The profile : " + createdServer.getName() + "has been added !", Toast.LENGTH_SHORT).show();
				}
				else{
					// We alarm the user of missing informations
					Toast.makeText(getApplicationContext(), "Missing informations ...", Toast.LENGTH_SHORT).show();

					// And indicate him of which informations we are lacking of (the color of the corresponding textviews becomes red)
					if(server_port.getText().length() == 0){
						server_port_TV.setTextColor(Color.RED);
						server_port_TV.invalidate();
						server_port.requestFocus();
					}
					if(server_Hostname.getText().length() == 0){
						server_Hostname_TV.setTextColor(Color.RED);
						server_Hostname_TV.invalidate();
						server_Hostname.requestFocus();
					}
					if(server_Name.getText().length() == 0){
						server_Name_TV.setTextColor(Color.RED);
						server_Name_TV.invalidate();
						server_Name.requestFocus();
					}
				}
				return handled;
			}
		});
	}
}