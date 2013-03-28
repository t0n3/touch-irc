package org.touchirc.irc;

import android.os.Binder;

public class IrcBinder extends Binder{
	
	private IrcService service;
	
	public IrcBinder(IrcService s){
		this.service = s;
	}
	
	public IrcService getService(){
		return this.service;
	}
	
	
	
	
}