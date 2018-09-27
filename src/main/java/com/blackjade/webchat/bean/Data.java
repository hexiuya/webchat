package com.blackjade.webchat.bean;

public class Data {
	private String time;
	private String from;
	private String text;
	private String id;
	
	public Data(){}
	
	public Data(String time,String from,String text,String id){
		this.time = time;
		this.from = from;
		this.text = text;
		this.id = id;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{time:" + time + ",from:" + from + ",text:" + text + ",id:" + id + "}";
	}
	
	

}
