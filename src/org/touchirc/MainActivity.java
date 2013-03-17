package org.touchirc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ListView lvListe;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Delete this when we will have thread
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		setContentView(R.layout.activity_main);

		// Lets go Irc connection
		MyIrcBot irc = new MyIrcBot(getApplicationContext());

		/*
        TextView textview = (TextView) findViewById(R.id.EditText1);
        textview.setText(irc.getNick());

        final Button button = (Button) findViewById(R.id.bouton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	TextView textview1 = (TextView) findViewById(R.id.EditText1);
            	TextView textview2 = (TextView) findViewById(R.id.editText3);
            	TextView textview3 = (TextView) findViewById(R.id.TextView1);
            	textview3.setText(textview1.getText().toString() + " veut se connecter à : " + textview2.getText().toString());
            }
        });


        TextView test = (TextView) findViewById(R.id.TextView1);
        TextView nickname = (TextView) findViewById(R.id.EditText1);
        TextView server = (TextView) findViewById(R.id.editText3);
        String s = (String) nickname.getText() + (String) server.getText();

        test.setText(s);
		 */

		Toast.makeText(this, "Welcome To Our App ! \\o/", Toast.LENGTH_LONG).show();

		lvListe = (ListView)findViewById(R.id.lvListe);
		final ArrayList<String> listeStrings = new ArrayList<String>();
		listeStrings.add("France");
		listeStrings.add("Switzerland");
		listeStrings.add("Italy");
		listeStrings.add("Norway");
		listeStrings.add("- * / 456 è _ à # {°]");
		
		lvListe.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listeStrings));		
		
		
		/*           -------------     NOT EFFICIENT YET       -----------
		
		final myAdapter adapter = new myAdapter(this, listeStrings);
		
		lvListe.setAdapter(adapter);
		
		*/

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView t = (TextView) findViewById(R.id.editText1);
				String newMsg = t.toString();
				listeStrings.add(newMsg);
				System.out.println(listeStrings.size());
				//adapter.notifyDataSetChanged();
			}
		});
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
