package com.blackjade.webchat.bean;

import java.text.SimpleDateFormat;

public class PushMessage {
	private String time;
	private String from;
	private String text;
	
	public PushMessage(){}
	
	public PushMessage(long time , String from , String text){
		
		SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    this.time = format0.format(time); 
	    this.from = from;
	    this.text = text;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
