package com.blackjade.webchat.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.nutz.ssdb4j.SSDBs;
import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blackjade.webchat.bean.DataContact;
import com.blackjade.webchat.bean.PushMessage;
import com.blackjade.webchat.bean.TalkMessage;

@Component
@Configuration
public class SSDBClient {
	/*public static void main(String[] args) {
		
		SSDB ssdb = SSDBs.simple("192.168.236.131",8888,2000);
		ssdb.set("name", "wendal").check(); // call check() to make sure resp is ok 

		Response resp = ssdb.get("name");
		if (!resp.ok()) {
		    // ...
		} else {
		    System.out.println("name=" + resp.asString());
		}
		try {
			ssdb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	@Value("${ssdb_url}")
    private  String ssdbUrl;
	@Value("${ssdb_port}")
    private  int ssdbPort;
	@Value("${ssdb_timeout}")
    private  int ssdbTimeout;
	
	private SSDB sSDB;
	
//	public SSDBClient(){
//		sSDB = getSSDB();
//	}
	
	@PostConstruct //加上该注解表明该方法会在bean初始化后调用
	private void init(){
	    //这里便可以获取到@Value变量
		sSDB = getSSDB();
	}
	
	public SSDB getSSDB(){
//		SSDB ssdb = SSDBs.simple("192.168.236.131",8888,2000);
//		SSDB ssdb = SSDBs.simple("192.168.1.2",8888,2000);
//		SSDB ssdb = SSDBs.simple("192.168.1.9",8888,2000);
		SSDB ssdb = SSDBs.simple(ssdbUrl,ssdbPort,ssdbTimeout);
		return ssdb;
	}
	
	public boolean userExist(String username){
		Response resp = sSDB.zget("all_users", username);
		
		if(resp.notFound()){
			return false;
		}
		return true;
	}
	
	public String addUserToDB(String uname){
		
		Response resp = sSDB.zset("all_users", uname, System.currentTimeMillis());
		
		if("ok".equals(resp.stat)){
			return "success";
		}
		
		return "fail";
	}
	
	public List<DataContact> getContacts(String key , int limit){
		
		Response resp = sSDB.zrscan(key, "" , "" , "" , limit);
		
//		List<String> list =  resp.listString();
		Map<String,String> maps = resp.mapString();
		Set<String> keys = maps.keySet();
		Iterator<String> iterator = keys.iterator();
		List<DataContact> data = new ArrayList<DataContact>();
		while(iterator.hasNext()){
			String resultKey = iterator.next();
			
			DataContact dataContact = new DataContact();
			dataContact.setName(resultKey);
			dataContact.setUnread(0);
			data.add(dataContact);
			
		}
		
		return data ;
		
		/*for(String str : list){
			DataContact dataContact = new DataContact();
			dataContact.setName(str);
			dataContact.setUnread(0);
			data.add(dataContact);
		}
		
		return data ;*/
	}
	
	public String getReadPosition(String uid, String uid2){
		String chatKey = chat_key(uid,uid2);
		String posKey = chatKey + "|read_pos";
		Response resp = sSDB.hget(posKey, uid);
		if(resp.notFound()){
			return "0";
		}
		return resp.asString();
	}
	
	public List listMessages(String uid, String uid2, int size, String max_msg_id){
		
		String chatKey = chat_key(uid, uid2);
		String readPos = getReadPosition(uid,uid2);
		Response resp = sSDB.hrscan(chatKey, max_msg_id, "", size);
//		List kvs = resp.listString();
		
		List<Map> ret = new ArrayList<Map>();
		
	    SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		
		Map<String,String> maps = resp.mapString();
		Set<String> keys = maps.keySet();
		Iterator<String> iterator = keys.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			System.out.println(key);
			
			String value = maps.get(key);
			
			Map map = (Map) JSON.parse(value);
			map.put("id", key);
			String time = format0.format(map.get("time"));
			
			map.put("time", time);
			
			String id = key;
			
			int unread = 1;
			if(id.equals(readPos)){
				unread = 0;
			}
			
			map.put("unread", unread);
			ret.add(map);
		}
		
		
	    
		
//		for(int i=0;i<kvs.size();i++){
//			String jsonStr = (String) kvs.get(i);
//			Map map = (Map) JSON.parse(jsonStr);
//			map.put("id", i);
//			String time = format0.format(map.get("time"));
//			
//			map.put("time", time);
//			
//			String id = i + "";
//			
//			int unread = 1;
//			if(id.equals(readPos)){
//				unread = 0;
//			}
//			
//			map.put("unread", unread);
//			ret.add(map);
//		}
		
		return ret;
	}
	
	public String chat_key(String uid1, String uid2){
		
		String[] arr = {uid1,uid2};
		
		Arrays.sort(arr);
		
		StringBuilder sb = new StringBuilder("chat|");
		
	    for(String str : arr){
	    	sb.append(str);
	    }
	    
	    return sb.toString();
	    
	}
	
	public String id_gen(){

		Response resp = sSDB.incr("msg_id_gen_0", 1);
		
		String msg_id_gen = System.currentTimeMillis() + "_" + resp.asString();
		
		return msg_id_gen;
	}
	
	public TalkMessage save_talk(String chat_key, String from, String text){
		
		String msg_id = id_gen();
		
		TalkMessage talkMessage = new TalkMessage(System.currentTimeMillis(),from,text,msg_id);
		
		String msg_str = JSONObject.toJSONString(talkMessage);
		
		
		sSDB.hset(chat_key, msg_id, msg_str);
		
		return talkMessage;
     }
	
	public void save_record(String from , String to , long time){
		sSDB.zset(recent_contacts_key(from), to, time);
		sSDB.zset(recent_contacts_key(to), from, time);
	}
	
	public String recent_contacts_key(String uid){
		return "recent_contacts|" + uid;
	}
	
	public String create_push_message(TalkMessage talkMessage){
		PushMessage pushMessage = new PushMessage(talkMessage.getTime(),talkMessage.getFrom(),talkMessage.getText());
		
		String msg_str = JSONObject.toJSONString(pushMessage);
		
		return msg_str;
	}
	
	public void close_ssdb_client(){
		try {
			sSDB.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
