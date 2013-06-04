package org.touchirc.model;


/**
 * 
 * 
 * profile_name : title done to the profile
 * first/second/thirdNick : nicknames chosen
 * listServer : list of servers associated to the profile since this one can own severals servers, each server in the list is unique
 *
 */

public class Profile {
	private String profile_name;
	private String firstNick;
	private String secondNick;
	private String thirdNick;
	private String username;
	private String realname;


	public Profile(String profile_n, String fnick, String snick, String tnick, String uname, String rname){
		this.profile_name = profile_n;
		this.firstNick = fnick;
		this.secondNick = snick;
		this.thirdNick = tnick;
		this.username = uname;
		this.realname = rname;		
	}

	public String getProfile_name() {
		return profile_name;
	}

	public void setProfile_name(String profile_name) {
		this.profile_name = profile_name;
	}

	public String getFirstNick() {
		return firstNick;
	}

	public void setFirstNick(String firstNick) {
		this.firstNick = firstNick;
	}

	public String getSecondNick() {
		return secondNick;
	}

	public void setSecondNick(String secondNick) {
		this.secondNick = secondNick;
	}

	public String getThirdNick() {
		return thirdNick;
	}

	public void setThirdNick(String thirdNick) {
		this.thirdNick = thirdNick;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
}
