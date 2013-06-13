package org.touchirc.model;

import java.security.Timestamp;
import java.util.Date;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.util.Linkify;
import android.widget.TextView;

public class Message {

	public final static int TYPE_MESSAGE = 0;
	public final static int TYPE_NOTICE = 1;
	public final static int TYPE_MENTION = 2;
	
	private String content;
	private String author;
	private int type;
	private long timestamp;
			
	public Message(String text) {
		this(text, null, TYPE_MESSAGE);
	}
	
	public Message(String text, String author) {
		this(text, author, TYPE_MESSAGE);
	}
	
	public Message(String text, int type) {
		this(text,null,type);
	}

	public Message(String text, String author, int type) {
		this.content = text;
		this.author = author;
		this.content = text;
		this.timestamp = new Date().getTime();
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public String getMessage(){
		return this.content;
	}
	
	public int getType(){
		return this.type;
	}
	
	private SpannableString getText(Context c){
		// TODO Add some colors, timestamp or not, etc
		
		String author = "<" + this.author + "> ";
		String message = content;
		String time = getTime(true, false) + " ";
		
		return new SpannableString(time + author + message);
	}
	
	public TextView getTextView(Context c){
		TextView t = new TextView(c);
		
		t.setAutoLinkMask(Linkify.ALL);
		t.setLinksClickable(true);
		// TODO Find a color for a link :)
		// t.setLinkTextColor(color);
		t.setText(getText(c));
		
		return t;
	}
	
	public String getTime(boolean use24, boolean includeSec)
    {
        Date date = new Date(timestamp);

        int hours = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();

        if (!use24) {
            hours = Math.abs(12 - hours);
            if (hours == 12) {
                hours = 0;
            }
        }

        if (includeSec) {
            return String.format("[%02d:%02d:%02d]",hours,minutes,seconds);
        } else {
            return String.format("[%02d:%02d]",hours,minutes);
        }
    }
	
}
