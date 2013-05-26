package org.touchirc;

import org.touchirc.db.Database;
import org.touchirc.model.Profile;
import org.touchirc.model.Server;

import android.content.Context;
import android.util.SparseArray;

public class TouchIrc{
	
	private static TouchIrc instance;
	
	private SparseArray<Server> availableServers;
	private SparseArray<Profile> availableProfiles;
	
	private int idDefaultProfile;
	private boolean loaded;
	
	public TouchIrc(){
		idDefaultProfile = -1; // set to null
		loaded = false;
	}
	

	public static TouchIrc getInstance(){
		if(instance == null)
			instance = new TouchIrc();
		
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
	
	public void addServer(Server server, Context c){
		Database db = new Database(c);
		db.addServer(server);
		loadServers(c);
		db.close();		
	}
	
	public boolean deleteServer(int idServer, Context c){
		Database db = new Database(c);
		boolean b = db.deleteServer(idServer);
		loadServers(c);
		db.close();	
		return b;
	}
	
	/*   PROFILES   */
	public SparseArray<Profile> getAvailableProfiles(){
		return availableProfiles;
	}
	
	public void addProfile(Profile profile, Context c){
		Database db = new Database(c);
		db.addProfile(profile);
		loadProfiles(c);
		db.close();		
	}
	
	public void deleteProfile(int idProfile, Context c){
		Database db = new Database(c);
		db.deleteProfile(idProfile);
		loadServers(c);
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


	public void load(Context c) {
		if(!loaded){
			loadProfiles(c);
			loadServers(c);
			loaded = true;
		}
	}
}
