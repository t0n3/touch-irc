package org.touchirc.view;

import java.util.LinkedList;

import org.touchirc.R;
import org.touchirc.model.Conversation;
import org.touchirc.model.Message;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ConversationActivity extends ListActivity {
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.conversation_display);

		final LinkedList<Message> values = new LinkedList<Message>();

		Message m1 = new Message("Android", "Bugdroid", 0);
		Message m2 = new Message("iPhone", "Steve Jobs", 0);
		Message m3 = new Message("WindowsMobile", "Bill Gates", 0);
		Message m4 = new Message("Blackberry", "BBy", 0);
		Message m5 = new Message("WebOS", "WebSO", 0);
		Message m6 = new Message("Ubuntu", "Unix", 0);
		Message m7 = new Message("Windows7", "Bill Gates", 0);
		Message m8 = new Message("Max OS X", "Paul Emploi", 0);
		Message m9 = new Message("Linux", "Tux", 0);
		Message m10 = new Message("OS/2" , "Steevy", 0);

		values.add(m1);
		values.add(m2);
		values.add(m3);
		values.add(m4);
		values.add(m5);
		values.add(m6);
		values.add(m7);
		values.add(m8);
		values.add(m9);
		values.add(m10);

		final ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
		
		/*			/--------- TODO ----------\
		 * 
		final EditText editText = (EditText) findViewById(R.id.messageToSend);
		editText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
		        boolean handled = false;
		        if (actionId == EditorInfo.IME_ACTION_SEND) {
		            values.add(new Message("You", editText.getText().toString(), 0));
		            adapter.notifyDataSetChanged();
		            handled = true;
		        }
		        return handled;
			}
		});
		*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
