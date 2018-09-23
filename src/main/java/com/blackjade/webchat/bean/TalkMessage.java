package com.blackjade.webchat.bean;

public class TalkMessage {
	private long time = System.currentTimeMillis();
	private String from;
	private String text;
	private String id;
	
	public TalkMessage(){}
	
	public TalkMessage(long time,String from,String text,String id){
		this.time = time;
		this.from = from;
		this.text = text;
		this.id = id;
	}
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
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
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
