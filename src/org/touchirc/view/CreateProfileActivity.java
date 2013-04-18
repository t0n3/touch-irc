package org.touchirc.view;

import org.touchirc.R;
import org.touchirc.model.Message;
import org.touchirc.model.Profile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class CreateProfileActivity extends Activity{

	private TextView profileName_TV;
	private EditText profileName;

	private TextView firstNickname_TV;
	private EditText firstNickname;

	private TextView secondNickname_TV;
	private EditText secondNickname;

	private TextView thirdNickname_TV;
	private EditText thirdNickname;

	private TextView userName_TV;
	private EditText userName;

	private TextView realName_TV;
	private EditText realName;

	private Profile createdProfile;
	
	private LinearLayout ll;


	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.createprofile_layout);
		
		// TextViews : black ones (default color)s are compulsories, gray ones are optionals

		this.profileName_TV = (TextView) findViewById(R.id.textView_profile_name);	

		this.firstNickname_TV = (TextView) findViewById(R.id.textView_first_nick);

		this.secondNickname_TV = (TextView) findViewById(R.id.textView_scnd_nick);
		this.secondNickname_TV.setTextColor(Color.GRAY);

		this.thirdNickname_TV = (TextView) findViewById(R.id.textView_third_nick);
		this.thirdNickname_TV.setTextColor(Color.GRAY);
		
		this.userName_TV = (TextView) findViewById(R.id.textView_username);
		
		this.realName_TV = (TextView) findViewById(R.id.textView_realname);
		
		// EditTexts		

		this.profileName = (EditText) findViewById(R.id.editText_profile_name);

		this.firstNickname = (EditText) findViewById(R.id.editText_first_nick);

		this.secondNickname = (EditText) findViewById(R.id.editText_scnd_nick);

		this.thirdNickname = (EditText) findViewById(R.id.editText_third_nick);

		this.userName = (EditText) findViewById(R.id.editText_username);

		this.realName = (EditText) findViewById(R.id.editText_realname);
		
		this.ll = (LinearLayout) findViewById(R.id.linearLayout);
		
		/**
		 * Create an OnEditorActionListener Object :
		 * When the Key "Done" is pressed on the Realname EditText : a Profile is created.
		 */

		this.realName.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				boolean handled = false;
				// the keyboard disappears
				InputMethodManager mngr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mngr.hideSoftInputFromWindow(realName.getWindowToken(), 0);
				if (arg1 == EditorInfo.IME_ACTION_DONE && (
						profileName.getText().length() != 0 && 
						firstNickname.getText().length() != 0 &&
						userName.getText().length() != 0 && 
						realName.getText().length() != 0)) {
					
					// the Profile is created with the datas given by the user
					createdProfile = new Profile(profileName.getText().toString(), firstNickname.getText().toString(), secondNickname.getText().toString(), thirdNickname.getText().toString(), userName.getText().toString(), realName.getText().toString());

					
					System.out.println("Profile Name = " + createdProfile.getProfile_name() + "\n1st Nick = " + createdProfile.getFirstNick() +
							"\n2nd Nick = " + createdProfile.getSecondNick() + "\n3rd Nick = " + createdProfile.getThirdNick() + "\nUsername = " + 
							createdProfile.getUsername() + "\nReal name = " + createdProfile.getRealname());

					handled = true;
					
					Toast.makeText(getApplicationContext(), "The profile : " + createdProfile.getProfile_name() + "has been added !", Toast.LENGTH_SHORT).show();
				}
				else{
					// We alarm the user of missing informations
					Toast.makeText(getApplicationContext(), "Missing informations ...", Toast.LENGTH_SHORT).show();
					
					// And indicate him of which informations we are lacking of
					if(realName.getText().length() == 0){
						realName_TV.setTextColor(Color.RED);
						realName_TV.invalidate();
						realName.requestFocus();
					}
					if(userName.getText().length() == 0){
						userName_TV.setTextColor(Color.RED);
						userName_TV.invalidate();
						userName.requestFocus();
					}
					if(firstNickname.getText().length() == 0){
						firstNickname_TV.setTextColor(Color.RED);
						firstNickname_TV.invalidate();
						firstNickname.requestFocus();
					}
					if(profileName.getText().length() == 0){
						profileName_TV.setTextColor(Color.RED);
						profileName_TV.invalidate();
						profileName.requestFocus();
					}
				}
				return handled;
			}
		});
	}
}
