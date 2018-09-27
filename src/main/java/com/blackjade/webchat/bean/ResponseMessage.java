package com.blackjade.webchat.bean;

import java.util.List;
import java.util.Map;

public class ResponseMessage extends ResponseCommon {
	
//	private List<DataMessage> data;
	private List<Map> data;
	
	public ResponseMessage(){}
	
	public ResponseMessage(int errno,String errmsg,List<Map> data){
		super.setErrno(errno); 
		super.setErrmsg(errmsg);
		this.data = data;
	}

	public List<Map> getData() {
		return data;
	}

	public void setData(List<Map> data) {
		this.data = data;
	}
	

}
