package org.touchirc.activity;

import org.touchirc.R;
import org.touchirc.TouchIrc;
import org.touchirc.model.Profile;
import org.touchirc.utils.Regex;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class CreateProfileActivity extends SherlockActivity{

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
	private Bundle bundleEdit = null;
	private Bundle bundleAddFromMenu = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Allows to the actionBar's icon to do "Previous"
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Create a Profile");

		// Prevents the keyboard to be displayed when you come on this activity
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.create_profile_layout);

		Intent i = getIntent();

		// Collect the Bundle object if the activity is started by ExistingProfilesActivity : Add or Edit
		bundleEdit = i.getBundleExtra("ProfileIdentification");
		bundleAddFromMenu = i.getBundleExtra("AddingFromExistingProfilesActivity");

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
		this.firstNickname_ET = (EditText) findViewById(R.id.editText_first_nick);
		this.secondNickname_ET = (EditText) findViewById(R.id.editText_second_nick);
		this.thirdNickname_ET = (EditText) findViewById(R.id.editText_third_nick);
		this.userName_ET = (EditText) findViewById(R.id.editText_username);
		this.realName_ET = (EditText) findViewById(R.id.editText_realname);

		// if started by ExistingProfilesActivity, changing the EditTexts'value
		if(bundleEdit != null && bundleEdit.containsKey("ProfileId")){
			// We collect the profile from available profiles list
			Profile profileToEdit = TouchIrc.getInstance().getAvailableProfiles().valueAt(bundleEdit.getInt("ProfileId"));

			// And put values in corresponding editText
			this.profileName_ET.setText(profileToEdit.getProfile_name());
			this.firstNickname_ET.setText(profileToEdit.getFirstNick());
			this.secondNickname_ET.setText(profileToEdit.getSecondNick());
			this.thirdNickname_ET.setText(profileToEdit.getThirdNick());
			this.userName_ET.setText(profileToEdit.getUsername());
			this.realName_ET.setText(profileToEdit.getRealname());
		}
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
				intent.setClass(this, ExistingProfilesActivity.class);
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;

		case R.id.save :

			if(addProfile()){				
				finish(); // close the activity
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void missingInformation() {
		// We alarm the user of missing informations
		Toast.makeText(getApplicationContext(), "Missing/Invalid informations ...", Toast.LENGTH_SHORT).show();

		// Initialize the color of the TV

		firstNickname_TV.setTextColor(Color.BLACK);
		realName_TV.setTextColor(Color.BLACK);

		// And indicate him of which informations we are lacking of (the color of the corresponding textviews becomes red)
		if(realName_ET.getText().length() == 0){
			realName_TV.setTextColor(Color.RED);
			realName_TV.invalidate();
			realName_ET.requestFocus();
		}
		if(userName_ET.getText().length() == 0){
			userName_TV.setTextColor(Color.RED);
			userName_TV.invalidate();
			userName_ET.requestFocus();
		}
		if(firstNickname_ET.getText().length() == 0){
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

	private boolean addProfile() {

		// Control the validity of the nicknames
		boolean validFirstNickname = Regex.isAValidNickName(firstNickname_ET.getText().toString());
		boolean validSecondNickname = Regex.isAValidNickName(secondNickname_ET.getText().toString());
		boolean validThirdNickname = Regex.isAValidNickName(thirdNickname_ET.getText().toString());

		if(validFirstNickname || validSecondNickname || validThirdNickname){
			// the Profile is created with the datas given by the user
			prof = new Profile(profileName_ET.getText().toString(),
					firstNickname_ET.getText().toString(),
					secondNickname_ET.getText().toString(),
					thirdNickname_ET.getText().toString(),
					userName_ET.getText().toString(),
					realName_ET.getText().toString());

			Intent i = new Intent(CreateProfileActivity.this, ExistingProfilesActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			if(bundleEdit != null){
				// Update the Profile in the database (+1 since the ID in Database begins at 0)
				TouchIrc.getInstance().updateProfile(bundleEdit.getInt("ProfileId")+1, prof, getApplicationContext());
				Toast.makeText(getApplicationContext(), "The profile : " + prof.getProfile_name() + " has been modified !", Toast.LENGTH_SHORT).show();

				startActivity(i);

			}
			else{
				// Add the Profile just created into the database
				TouchIrc.getInstance().addProfile(prof, this);
				Toast.makeText(getApplicationContext(), "The profile : " + prof.getProfile_name() + " has been added !", Toast.LENGTH_SHORT).show();

				if(bundleAddFromMenu == null){
					i.setClass(getApplicationContext(), MenuActivity.class);
				}
				startActivity(i);
			}

			// No problems concerning values
			return true;

		}
		else{
			if(validFirstNickname){
				this.firstNickname_ET.setTextColor(Color.RED);
				this.firstNickname_ET.invalidate();
			}
			if(validSecondNickname){
				this.secondNickname_ET.setTextColor(Color.RED);
				this.secondNickname_ET.invalidate();
			}
			if(validThirdNickname){
				this.thirdNickname_ET.setTextColor(Color.RED);
				this.thirdNickname_ET.invalidate();
			}

			missingInformation();

			// Some values are missing/invalids
			return false;
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
				intent.setClass(this, ExistingProfilesActivity.class);
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
