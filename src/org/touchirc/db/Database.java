package org.touchirc.db;

import java.util.ArrayList;

import org.touchirc.model.Profile;
import org.touchirc.model.Server;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private static final String DB_NAME = "touchirc.db";
	private static final int DB_VERSION = 1;

	public Database(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE servers (" + DBConstants.SERVER_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstants.SERVER_TITLE + " TEXT NOT NULL,"
				+ DBConstants.SERVER_HOST + " TEXT NOT NULL,"
				+ DBConstants.SERVER_PORT + " INTEGER,"
				+ DBConstants.SERVER_PASSWORD + " TEXT, "
				+ DBConstants.SERVER_USE_SSL + " BOOLEAN, "
				+ DBConstants.SERVER_CHARSET + " TEXT,"
				+ DBConstants.SERVER_AUTOCONNECT + " BOOLEAN );");

		db.execSQL("CREATE TABLE " + DBConstants.PROFILE_TABLE_NAME + "(" + DBConstants.PROFILE_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstants.PROFILE_NAME + " TEXT NOT NULL,"
				+ DBConstants.PROFILE_FIRST_NICKNAME + " TEXT NOT NULL,"
				+ DBConstants.PROFILE_SCD_NICKNAME + " TEXT,"
				+ DBConstants.PROFILE_THIRD_NICKNAME + " TEXT,"
				+ DBConstants.PROFILE_USERNAME + " TEXT NOT NULL,"
				+ DBConstants.PROFILE_REALNAME + " TEXT NOT NULL,"
				+ DBConstants.DEFAULT_PROFILE + " BOOLEAN,"
				+ DBConstants.PROFILE_SERVER_LIST + " TEXT );");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * Add the given server to the database
	 * 
	 * @param server
	 */
	public void addServer(Server server) {

		ContentValues values = new ContentValues();

		values.put(DBConstants.SERVER_TITLE, server.getName());
		values.put(DBConstants.SERVER_HOST, server.getHost());
		values.put(DBConstants.SERVER_PORT, server.getPort());
		values.put(DBConstants.SERVER_PASSWORD, server.getPassword());
		values.put(DBConstants.SERVER_USE_SSL, server.useSSL());
		values.put(DBConstants.SERVER_CHARSET, server.getEncoding());
		values.put(DBConstants.SERVER_AUTOCONNECT, false);

		this.getWritableDatabase().insert(DBConstants.SERVER_TABLE_NAME, null,
				values);

	}

	/**
	 * Return a server object with the given serverId
	 * 
	 * @param serverId
	 * @return
	 */
	public Server getServerById(int serverId) {
		Server server = null;

		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.SERVER_TABLE_NAME, DBConstants.SERVER_ALL,
				DBConstants.SERVER_ID + " = " + serverId, null, null, null,
				DBConstants.SERVER_TITLE + " ASC");

		if (cursor.moveToNext()) {
			server = createServer(cursor);
		}

		cursor.close();

		return server;
	}
	
	/**
	 * Return a server object with the given serverName
	 * 
	 * @param serverName
	 * @return
	 */
	public Server getServerByName(String serverName) {
		Server server = null;

		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.SERVER_TABLE_NAME, DBConstants.SERVER_ALL,
				DBConstants.SERVER_TITLE + " = " + "\"" + serverName + "\"", null, null, null,
				DBConstants.SERVER_TITLE + " ASC");

		if (cursor.moveToNext()) {
			server = createServer(cursor);
		}

		cursor.close();

		return server;
	}
	
	/**
	 * Update the values of the current server
	 * 
	 * @param s
	 * @return boolean
	 */
	
	public boolean updateServer(Server s, String oldNameServer){
	    ContentValues newValues = new ContentValues();
	    newValues.put(DBConstants.SERVER_TITLE, s.getName());
	    newValues.put(DBConstants.SERVER_HOST, s.getHost());
	    newValues.put(DBConstants.SERVER_PORT, s.getPort());
	    newValues.put(DBConstants.SERVER_PASSWORD, s.getPassword());
	    
	    if(this.getWritableDatabase().update(	DBConstants.SERVER_TABLE_NAME,
	    										newValues, 
	    										DBConstants.SERVER_TITLE + "=" + "\"" + oldNameServer + "\"",
	    										null) > 0){
	    	return true;
	    }

	    return false;
	}

	public ArrayList<Server> getServerList() {

		ArrayList<Server> listServer = new ArrayList<Server>();

		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.SERVER_TABLE_NAME, DBConstants.SERVER_ALL, null,
				null, null, null, DBConstants.SERVER_TITLE + " ASC");

		while (cursor.moveToNext()) {
			listServer.add(createServer(cursor));
		}

		cursor.close();

		return listServer;
	}

	/**
	 * Create a server object with the given database cursor
	 * 
	 * @param cursor
	 * @return Server
	 */
	private Server createServer(Cursor cursor) {

		Server server = new Server(
				cursor.getString(cursor
						.getColumnIndex((DBConstants.SERVER_TITLE))),
						cursor.getString(cursor
								.getColumnIndex((DBConstants.SERVER_HOST))),
								cursor.getInt(cursor.getColumnIndex((DBConstants.SERVER_PORT))),
								cursor.getString(cursor
										.getColumnIndex(DBConstants.SERVER_PASSWORD)), cursor
										.getString(cursor
												.getColumnIndex(DBConstants.SERVER_CHARSET)));

		// TODO SSL Support

		return server;
	}
	
	/**
	 * Delete the Server from the database
	 * the method delete returns the number of rows deleted inside the database
	 * 
	 * @param serverName
	 * @return Boolean
	 */
	
	public boolean deleteServer(String serverName){
		return this.getWritableDatabase().delete(DBConstants.SERVER_TABLE_NAME, 
												 DBConstants.SERVER_TITLE + "=\"" + serverName + "\"", 
												 null
												 ) > 0;
	}
	
	/**
	 * Return the name of the default profile
	 * SQLite compare the booleans thanks to String
	 * that is why the query used  "\"" + true + "\""
	 * 
	 * @return boolean
	 */
	
	public String nameAutoConnectedServer(){
		
		Server autoConnectedServer = null;
		
		// SELECT ALL FROM servers WHERE autoconnect="true" ORDER BY name ASC
		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.SERVER_TABLE_NAME, DBConstants.SERVER_ALL,
				DBConstants.SERVER_AUTOCONNECT + " = " + "\"" + true + "\"", null, null, null,
				DBConstants.SERVER_TITLE + " ASC");
		
		// if the query returns 1 row
		if (cursor.getCount() == 1) {
			cursor.moveToNext(); // To avoid out of bounds exception
			autoConnectedServer = createServer(cursor);
			return autoConnectedServer.getName();
		}

		return null;
	}
	
	public boolean setAutoConnect(String newAutoConnectedServerName){
		
		// Checking the presence of an auto-connected server
		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.SERVER_TABLE_NAME, DBConstants.SERVER_ALL,
				DBConstants.SERVER_AUTOCONNECT + " = " + "\"" + true + "\"", null, null, null,
				DBConstants.SERVER_TITLE + " ASC");
		
		// if an auto-connected server already exists
		if(cursor.getCount() > 0){
		
			// The old auto-connected server becomes a standard server
		    ContentValues oldAutoConnectedServer = new ContentValues();
		    oldAutoConnectedServer.put(DBConstants.SERVER_AUTOCONNECT, "false");
			this.getWritableDatabase().update(	DBConstants.SERVER_TABLE_NAME,
												oldAutoConnectedServer,
												DBConstants.SERVER_AUTOCONNECT + "=" + "\"" + true + "\"",
												null);
		}
		
		// Setting the new auto-connected server
	    ContentValues newAutoConnectedServer = new ContentValues();
	    newAutoConnectedServer.put(DBConstants.SERVER_AUTOCONNECT, "true");
	    if(this.getWritableDatabase().update(	DBConstants.SERVER_TABLE_NAME,
	    										newAutoConnectedServer, 
	    										DBConstants.SERVER_TITLE + "=" + "\"" + newAutoConnectedServerName + "\"",
	    										null) > 0){
	    	return true;
	    }
		
	    return false;
	}

	/**
	 * Add the given profile to the database
	 * 
	 * @param profile
	 */
	public void addProfile(Profile profile) {

		ContentValues values = new ContentValues();

		values.put(DBConstants.PROFILE_NAME, profile.getProfile_name());
		values.put(DBConstants.PROFILE_FIRST_NICKNAME, profile.getFirstNick());
		values.put(DBConstants.PROFILE_SCD_NICKNAME, profile.getSecondNick());
		values.put(DBConstants.PROFILE_THIRD_NICKNAME, profile.getThirdNick());
		values.put(DBConstants.PROFILE_USERNAME, profile.getUsername());
		values.put(DBConstants.PROFILE_REALNAME, profile.getRealname());
		values.put(DBConstants.DEFAULT_PROFILE, "false");
		// To check : getListServer.TOSTRING() \\
		values.put(DBConstants.PROFILE_SERVER_LIST, profile.getListServer().toString());

		this.getWritableDatabase().insert(DBConstants.PROFILE_TABLE_NAME, null,
				values);

	}

	/**
	 * Return a profile object with the given profileId
	 * 
	 * @param profileId
	 * @return
	 */
	public Profile getProfileById(int profileId) {
		Profile profile = null;

		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.PROFILE_TABLE_NAME, DBConstants.PROFILE_ALL,
				DBConstants.PROFILE_ID + " = " + profileId, null, null, null,
				DBConstants.PROFILE_NAME + " ASC");

		if (cursor.moveToNext()) {
			profile = createProfile(cursor);
		}

		cursor.close();

		return profile;
	}
	
	/**
	 * Return a profile object with the given profileName
	 * 
	 * @param profileName
	 * @return
	 */
	public Profile getProfileByName(String profileName) {
		Profile profile = null;

		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.PROFILE_TABLE_NAME, DBConstants.PROFILE_ALL,
				DBConstants.PROFILE_NAME + " = " + "\"" + profileName + "\"", null, null, null,
				DBConstants.PROFILE_NAME + " ASC");

		if (cursor.moveToNext()) {
			profile = createProfile(cursor);
		}

		cursor.close();

		return profile;
	}

	public ArrayList<Profile> getProfileList() {

		ArrayList<Profile> listProfile = new ArrayList<Profile>();

		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.PROFILE_TABLE_NAME, DBConstants.PROFILE_ALL, null,
				null, null, null, DBConstants.PROFILE_NAME + " ASC");

		while (cursor.moveToNext()) {
			listProfile.add(createProfile(cursor));
		}
		cursor.close();

		return listProfile;
	}

	/**
	 * Create a profile object with the given database cursor
	 * 
	 * @param cursor
	 * @return Profile
	 */
	private Profile createProfile(Cursor cursor) {

		Profile profile = new Profile(
				cursor.getString(cursor.getColumnIndex(DBConstants.PROFILE_NAME)),
				cursor.getString(cursor.getColumnIndex(DBConstants.PROFILE_FIRST_NICKNAME)),
				cursor.getString(cursor.getColumnIndex(DBConstants.PROFILE_SCD_NICKNAME)),
				cursor.getString(cursor.getColumnIndex(DBConstants.PROFILE_THIRD_NICKNAME)),
				cursor.getString(cursor.getColumnIndex(DBConstants.PROFILE_USERNAME)),
				cursor.getString(cursor.getColumnIndex(DBConstants.PROFILE_USERNAME))
				);
		return profile;
	}
	
	/**
	 * Delete the Profile from the database
	 * the method delete returns the number of rows deleted inside the database
	 * 
	 * @param profileName
	 * @return Boolean
	 */
	
	public boolean deleteProfile(String profileName){
		return this.getWritableDatabase().delete(DBConstants.PROFILE_TABLE_NAME, 
												 DBConstants.PROFILE_NAME + "=\"" + profileName + "\"", 
												 null
												 ) > 0;
	}
	
	/**
	 * Return the name of the default profile
	 * SQLite compare the booleans thanks to String
	 * that is why the query used  "\"" + true + "\""
	 * 
	 * @return boolean
	 */
	
	public String nameDefaultProfile(){
		
		Profile defaultProfile = null;
		
		// SELECT ALL FROM profiles WHERE profile_by_default="true" ORDER BY name ASC
		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.PROFILE_TABLE_NAME, DBConstants.PROFILE_ALL,
				DBConstants.DEFAULT_PROFILE + " = " + "\"" + true + "\"", null, null, null,
				DBConstants.PROFILE_NAME + " ASC");
		
		// if the query returns 1 row
		if (cursor.getCount() == 1) {
			cursor.moveToNext(); // To avoid out of bounds exception
			defaultProfile = createProfile(cursor);
			return defaultProfile.getProfile_name();
		}

		return null;
	}
	
	public boolean setDefaultProfile(String newDefaultProfileName){
		
		// Checking the presence of a default profile
		Cursor cursor = this.getReadableDatabase().query(
				DBConstants.PROFILE_TABLE_NAME, DBConstants.PROFILE_ALL,
				DBConstants.DEFAULT_PROFILE + " = " + "\"" + true + "\"", null, null, null,
				DBConstants.PROFILE_NAME + " ASC");
		
		// if a default profile already exists
		if(cursor.getCount() > 0){
		
			// The old default profile becomes a standard profile
		    ContentValues oldDefaultProfile = new ContentValues();
		    oldDefaultProfile.put(DBConstants.DEFAULT_PROFILE, "false");
			this.getWritableDatabase().update(	DBConstants.PROFILE_TABLE_NAME,
												oldDefaultProfile,
												DBConstants.DEFAULT_PROFILE + "=" + "\"" + true + "\"",
												null);
		}
		
		// Setting the new default Profile
	    ContentValues newDefaultProfile = new ContentValues();
	    newDefaultProfile.put(DBConstants.DEFAULT_PROFILE, "true");
	    if(this.getWritableDatabase().update(	DBConstants.PROFILE_TABLE_NAME,
	    									newDefaultProfile, 
	    									DBConstants.PROFILE_NAME + "=" + "\"" + newDefaultProfileName + "\"",
	    									null) > 0){
	    	return true;
	    }
		
	    return false;
	}
	
	/**
	 * Update the values of the current profile
	 * 
	 * @param p
	 * @return boolean
	 */
	
	public boolean updateProfile(Profile p, String oldeNameProfile){
	    ContentValues newValues = new ContentValues();
	    newValues.put(DBConstants.PROFILE_NAME, p.getProfile_name());
	    newValues.put(DBConstants.PROFILE_FIRST_NICKNAME, p.getFirstNick());
	    newValues.put(DBConstants.PROFILE_SCD_NICKNAME, p.getSecondNick());
	    newValues.put(DBConstants.PROFILE_THIRD_NICKNAME, p.getThirdNick());
	    newValues.put(DBConstants.PROFILE_USERNAME, p.getUsername());
	    newValues.put(DBConstants.PROFILE_REALNAME, p.getRealname());
	    
	    if(this.getWritableDatabase().update(	DBConstants.PROFILE_TABLE_NAME,
	    										newValues, 
	    										DBConstants.PROFILE_NAME + "=" + "\"" + oldeNameProfile + "\"",
	    										null) > 0){
	    	return true;
	    }

	    return false;
	}
}
