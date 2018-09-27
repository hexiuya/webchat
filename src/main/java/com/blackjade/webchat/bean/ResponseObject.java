package com.blackjade.webchat.bean;


public class ResponseObject extends ResponseCommon{
	private Data data;
	
	public ResponseObject(){}
	
	public ResponseObject(int errno,String errmsg,Data data){
		super.setErrno(errno); 
		super.setErrmsg(errmsg);
		this.data = data;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}
