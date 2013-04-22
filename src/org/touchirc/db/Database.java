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
				+ DBConstants.SERVER_CHARSET + " TEXT );");

		db.execSQL("CREATE TABLE" + DBConstants.PROFILE_TABLE_NAME + "(" + DBConstants.PROFILE_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstants.PROFILE_NAME + " TEXT NOT NULL,"
				+ DBConstants.PROFILE_FIRST_NICKNAME + " TEXT NOT NULL,"
				+ DBConstants.PROFILE_SCD_NICKNAME + " TEXT,"
				+ DBConstants.PROFILE_THIRD_NICKNAME + " TEXT,"
				+ DBConstants.PROFILE_USERNAME + " TEXT NOT NULL,"
				+ DBConstants.PROFILE_REALNAME + " TEXT NOT NULL,"
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
}
