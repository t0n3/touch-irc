package org.touchirc;

import org.touchirc.db.Database;
import org.touchirc.model.Profile;
import org.touchirc.model.Server;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;

public class TouchIrc extends Activity{
	
	private static TouchIrc instance;
	
	private Context context;
	
	private SparseArray<Server> availableServers;
	private SparseArray<Profile> availableProfiles;
	
	private int idDefaultProfile;
	
	public TouchIrc(Context c){
		idDefaultProfile = -1; // set to null

		loadProfiles(c);
		loadServers(c);
			
	}
	

	public static TouchIrc getInstance(Context c){
		if(instance == null)
			instance = new TouchIrc(c);
		
		return instance;
	}
	


	private void loadServers(Context c) {
		Database db = new Database(c);
		availableServers = db.getServerList();
		db.close();		

	}
	
	private void loadProfiles(Context c) {
		Database db = new Database(c);
		availableProfiles = db.getProfileList();
		db.close();		

		// TODO Load default profile
	}
	
	/*   SERVERS   */
	public SparseArray<Server> getAvailableServers(){
		return availableServers;
	}
	
	public void addServer(Server server){
		Database db = new Database(context);
		db.addServer(server);
		loadServers(context);
		db.close();		
	}
	
	public boolean deleteServer(int idServer){
		Database db = new Database(context);
		boolean b = db.deleteServer(idServer);
		loadServers(context);
		db.close();	
		return b;
	}
	
	/*   PROFILES   */
	public SparseArray<Profile> getAvailableProfiles(){
		return availableProfiles;
	}
	
	public void addProfile(Profile profile){
		Database db = new Database(context);
		db.addProfile(profile);
		loadServers(context);
		db.close();		
	}
	
	public void deleteProfile(int idProfile){
		Database db = new Database(context);
		db.deleteProfile(idProfile);
		loadServers(context);
		db.close();	
	}
	
	public int getDefaultProfile(){
		return (idDefaultProfile != -1) ? idDefaultProfile : 1;
	}


	public boolean setDefaultProfile(int id) {
		idDefaultProfile = id;
		// TODO 
		return true;
	}
}
