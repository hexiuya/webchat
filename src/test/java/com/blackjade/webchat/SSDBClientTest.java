package com.blackjade.webchat;

import org.junit.Before;
import org.junit.Test;
import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;

import com.blackjade.webchat.util.SSDBClient;

public class SSDBClientTest {
	private SSDB sSDB;
	
	@Before
	public void setSSDBClient(){
		sSDB = new SSDBClient().getSSDB(); 
	}
	
	@Test
	public void testIncr(){
		Response resp = sSDB.ignore_key_range();
		System.out.println(resp.asString());
		resp = sSDB.ignore_key_range();
		System.out.println(resp.asString());
	}
	
	@Test
	public void testIncr2(){
		//incr(String key, long by)  第一个参数是key,第二个参数是增长步数 step
		Response resp = sSDB.incr("a", 1);
		System.out.println(resp.asString());
		resp = sSDB.incr("a", 1);
		System.out.println(resp.asString());
	}
	
	@Test
	public void testDelKey(){
		Response resp = sSDB.del("a");
		
		System.out.println(resp.asString());
	}
	
	@Test
	public void testAddUserToDB(){
		
		Response resp = sSDB.zset("all_users", "chaolumen", System.currentTimeMillis());
		
		System.out.println(resp.stat);
		
		if("ok".equals(resp.stat)){
			
			System.out.println("success");
			
		}else{
			System.out.println("fail");
		}
		
		
	}
	
	@Test
	public void zrscan(){
		String uid = "boy2";
		String key = "recent_contacts|" + uid;
		int limit = 10 ; 
		
		Response resp = sSDB.zrscan(key, null , null , null , limit);
		
		System.out.println(resp);
	}
}
