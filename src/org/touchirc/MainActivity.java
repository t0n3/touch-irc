package org.touchirc;

import org.touchirc.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

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

        
        TextView textview = (TextView) findViewById(R.id.textview);
        textview.setText(irc.getNick());  
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
}
