package org.touchirc.activity;

import org.touchirc.R;
import org.touchirc.TouchIrc;
import org.touchirc.irc.IrcBinder;
import org.touchirc.irc.IrcBot;
import org.touchirc.irc.IrcService;
import org.touchirc.model.Server;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.SparseArray;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LauncherActivity extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);
		
		new LoadingTask().execute();
	}
	
	private class LoadingTask extends AsyncTask<Void, Integer, Long> implements ServiceConnection {
		String[] messages = {"Loading db", "Starting the Service","Service Started","", "Launching Activity", "Done ?!"};
		private IrcService ircService;
		
		protected Long doInBackground(Void... useless) {
			if(isMyServiceRunning()){
				Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				getApplicationContext().startActivity(intent);
			}
			publishProgress(0);
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			TouchIrc.getInstance().load(getApplicationContext());
			publishProgress(1);
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			Intent intentService = new Intent(getApplicationContext(), IrcService.class);
			getApplicationContext().startService(intentService);
			getApplicationContext().bindService(intentService, this, 0);
			while(ircService == null);
			publishProgress(2);
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			boolean autoconnectedServer = false;
			SparseArray<Server> availableServers = TouchIrc.getInstance().getAvailableServers();
			for(int i = 0 ; i < availableServers.size() ; i++)
				if(availableServers.valueAt(i).isAutoConnect()){
					autoconnectedServer = true;
					break;
				}
			if(autoconnectedServer){
				publishProgress(3);
				for(int i = 0 ; i < availableServers.size() ; i++)
					if(availableServers.valueAt(i).isAutoConnect()){
						IrcBot bot = ircService.getBot(availableServers.valueAt(i));
						Integer[] msg = {99, i};
						publishProgress(msg);
						do{
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {}
						}while(!bot.isConnected);
					}
				publishProgress(4);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				getApplicationContext().startActivity(intent);
				
			}else{
				publishProgress(4);
				Intent intent = new Intent(getApplicationContext(), ExistingServersActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				getApplicationContext().startActivity(intent);
			}
			
			
		         
		   return (long) 5;
		 }
		
		 protected void onProgressUpdate(Integer... progress) {
			 if(progress[0] == 99){
				 String msg = TouchIrc.getInstance().getAvailableServers().valueAt(progress[1]).getName();
				 ((TextView) findViewById(R.id.loadingTextView)).setText("Attempt to connect to : " + msg);
			 }else{
		    	 ((ProgressBar) findViewById(R.id.loadingProgressBar)).setProgress(progress[0]);
		    	 ((TextView) findViewById(R.id.loadingTextView)).setText(messages[progress[0]]);
			 }
		 }
		
		 protected void onPostExecute(Long result) {
		 }

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			ircService = ((IrcBinder) binder).getService();
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		private boolean isMyServiceRunning() {
		    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		        if ("org.touchirc.irc.IrcService".equals(service.service.getClassName()))
		            return true;
		    return false;
		}
		
	 }
 
}
