package com.blackjade.webchat.bean;

import java.util.List;

public class ResponseContact extends ResponseCommon{
	private List<DataContact> data ;
	
	public ResponseContact(){}
	
	public ResponseContact(int errno,String errmsg,List<DataContact> data){
		super.setErrno(errno); 
		super.setErrmsg(errmsg);
		this.data = data;
	}

	public List<DataContact> getData() {
		return data;
	}

	public void setData(List<DataContact> data) {
		this.data = data;
	}

}
