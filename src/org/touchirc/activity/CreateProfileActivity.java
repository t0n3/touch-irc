package org.touchirc.activity;

import org.touchirc.R;
import org.touchirc.db.Database;
import org.touchirc.model.Profile;

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
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class CreateProfileActivity extends Activity{

	private TextView profileName_TV;
	private EditText profileName_ET;

	private TextView firstNickname_TV;
	private EditText firstNickname_ET;

	private TextView secondNickname_TV;
	private EditText secondNickname_ET;

	private TextView thirdNickname_TV;
	private EditText thirdNickname_ET;

	private TextView userName_TV;
	private EditText userName_ET;

	private TextView realName_TV;
	private EditText realName_ET;

	private Profile prof;
	private Bundle b = null;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.create_profile_layout);

		Intent i = getIntent();

		// Collect the Bundle object if the activity is started by ExistingServersActivity
		b = i.getBundleExtra("ProfileIdentification");

		// TextViews : black ones (default color)s are compulsories, gray ones are optionals

		this.profileName_TV = (TextView) findViewById(R.id.textView_profile_name);	

		this.firstNickname_TV = (TextView) findViewById(R.id.textView_first_nick);

		this.secondNickname_TV = (TextView) findViewById(R.id.textView_second_nick);
		this.secondNickname_TV.setTextColor(Color.GRAY);

		this.thirdNickname_TV = (TextView) findViewById(R.id.textView_third_nick);
		this.thirdNickname_TV.setTextColor(Color.GRAY);

		this.userName_TV = (TextView) findViewById(R.id.textView_username);

		this.realName_TV = (TextView) findViewById(R.id.textView_realname);

		// EditTexts		

		this.profileName_ET = (EditText) findViewById(R.id.editText_profile_name);
		// if started by ExistingProfilesActivity, changing the EditText
		if(b != null && b.containsKey("ProfileName")){
			this.profileName_ET.setText(b.getString("ProfileName"));
		}

		this.firstNickname_ET = (EditText) findViewById(R.id.editText_first_nick);
		// if started by ExistingProfilesActivity, changing the EditText
		if(b != null && b.containsKey("FirstNickName")){
			this.firstNickname_ET.setText(b.getString("FirstNickName"));
		}

		this.secondNickname_ET = (EditText) findViewById(R.id.editText_second_nick);
		// if started by ExistingProfilesActivity, changing the EditText
		if(b != null && b.containsKey("ScdNickName") && b.getString("ScdNickName").length() > 0){
			this.secondNickname_ET.setText(b.getString("ScdNickName"));
			this.secondNickname_TV.setTextColor(Color.BLACK); // To highlight the fact that a 2nd nick exists
		}

		this.thirdNickname_ET = (EditText) findViewById(R.id.editText_third_nick);
		// if started by ExistingProfilesActivity, changing the EditText
		if(b != null && b.containsKey("ThdNickName") && b.getString("ThdNickName").length() > 0){
			this.thirdNickname_ET.setText(b.getString("ThdNickName"));
			this.thirdNickname_TV.setTextColor(Color.BLACK); // To highlight the fact that a 3rd nick exists
		}

		this.userName_ET = (EditText) findViewById(R.id.editText_username);
		// if started by ExistingProfilesActivity, changing the EditText
		if(b != null && b.containsKey("UserName")){
			this.userName_ET.setText(b.getString("UserName"));
		}

		this.realName_ET = (EditText) findViewById(R.id.editText_realname);
		// if started by ExistingProfilesActivity, changing the EditText
		if(b != null && b.containsKey("RealName")){
			this.realName_ET.setText(b.getString("RealName"));
		}

		/**
		 * Create an OnEditorActionListener Object :
		 * When the Key "Done" is pressed on the Realname EditText : a Profile is created.
		 */

		this.realName_ET.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				boolean handled = false;

				// the keyboard disappears
				InputMethodManager mngr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mngr.hideSoftInputFromWindow(realName_ET.getWindowToken(), 0);

				if (arg1 == EditorInfo.IME_ACTION_DONE && (
						profileName_ET.getText().length() != 0 && 
						firstNickname_ET.getText().length() != 0 &&
						firstNickname_ET.getText().length() <= 9 &&
						userName_ET.getText().length() != 0 && 
						realName_ET.getText().length() != 0 &&
						realName_ET.getText().length() >= 10 &&
						realName_ET.getText().length() < 20)) {

					// the Profile is created with the datas given by the user
					prof = new Profile(profileName_ET.getText().toString(),
							firstNickname_ET.getText().toString(),
							secondNickname_ET.getText().toString(),
							thirdNickname_ET.getText().toString(),
							userName_ET.getText().toString(),
							realName_ET.getText().toString());

					Database db = new Database(getApplicationContext());
					
					if(b != null){
						// Update the Profile in the database
						db.updateProfile(prof, b.getString("ProfileName"));
						Toast.makeText(getApplicationContext(), "The server : " + prof.getProfile_name() + " has been modified !", Toast.LENGTH_SHORT).show();
						
						Intent i = new Intent(CreateProfileActivity.this, ExistingProfilesActivity.class);
						// Use a Bundle to transfer the profile modified
						Bundle b = new Bundle();
						b.putString("NewNameProfile", prof.getProfile_name());
						i.putExtra("NewValue", b);
						startActivity(i);
					}
					else{
						// Add the Server just created into the database
						db.addProfile(prof);
						Toast.makeText(getApplicationContext(), "The profile : " + prof.getProfile_name() + " has been added !", Toast.LENGTH_SHORT).show();
					}
					
					db.close();
					
					handled = true;					
					finish(); // close the activity
				}
				else{
					// We alarm the user of missing informations
					Toast.makeText(getApplicationContext(), "Missing/Invalid informations ...", Toast.LENGTH_SHORT).show();
					
					// Initialize the color of the TV
					
					firstNickname_TV.setTextColor(Color.BLACK);
					realName_TV.setTextColor(Color.BLACK);

					// And indicate him of which informations we are lacking of (the color of the corresponding textviews becomes red)
					if(realName_ET.getText().length() == 0 || realName_ET.getText().length() < 10 || realName_ET.getText().length() > 20){
						realName_TV.setTextColor(Color.RED);
						realName_TV.invalidate();
						realName_ET.requestFocus();
					}
					if(userName_ET.getText().length() == 0){
						userName_TV.setTextColor(Color.RED);
						userName_TV.invalidate();
						userName_ET.requestFocus();
					}
					if(firstNickname_ET.getText().length() == 0 || firstNickname_ET.getText().length() > 9){
						firstNickname_TV.setTextColor(Color.RED);
						firstNickname_TV.invalidate();
						firstNickname_ET.requestFocus();
					}
					if(profileName_ET.getText().length() == 0){
						profileName_TV.setTextColor(Color.RED);
						profileName_TV.invalidate();
						profileName_ET.requestFocus();
					}
				}
				return handled;
			}
		});
	}
}
