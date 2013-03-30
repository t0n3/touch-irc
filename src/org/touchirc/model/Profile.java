package org.touchirc.model;

import java.util.ArrayList;

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
	
	public ArrayList<Server> listServer;
	
	public Profile(String profile_n, String fnick, String snick, String tnick, String uname, String rname){
		this.profile_name = profile_n;
		this.firstNick = fnick;
		this.secondNick =  snick;
		this.thirdNick =  tnick;
		this.username = uname;
		this.realname = rname;
		this.listServer = new ArrayList<Server>();
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
	
	public boolean addServer(Server s, String pwd){
		int i = 0;
		// check if the server exists in the list
		while(i < this.listServer.size() && this.listServer.get(i).getName() != s.getName()){
			i++;
		}
		// server found since it always exists in the list
		if(this.listServer.get(i).getName() == s.getName()){
			return false;
		}
		// new server associated to the current profile
		else{
			if(s.getPassword() == pwd){
				this.listServer.add(s);
				return true;
			}
			return false;
		}
	}
	
	public boolean deleteServer(Server s){
		for(Server serv : this.listServer){
			if(serv.getName() == s.getName()){
				this.listServer.remove(serv);
				return true;
			}
		}
		return false;
	}
	
	// with new datas sent by the user, create a new Server which will take the place of the older one
	public boolean modifyServer(Server oldServ, Server newServ){
		int i = 0;
		// check if the server exists in the list
		while(i < this.listServer.size() && this.listServer.get(i).getName() != oldServ.getName()){
			i++;
		}
		// server found since it always exists in the list
		if(this.listServer.get(i).getName() == oldServ.getName()){
			this.listServer.set(i, newServ);
			return true;
		}
		return false;
	}
}
