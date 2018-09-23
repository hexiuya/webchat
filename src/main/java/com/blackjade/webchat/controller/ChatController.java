package com.blackjade.webchat.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.blackjade.webchat.bean.Data;
import com.blackjade.webchat.bean.DataContact;
import com.blackjade.webchat.bean.ResponseContact;
import com.blackjade.webchat.bean.ResponseMessage;
import com.blackjade.webchat.bean.ResponseObject;
import com.blackjade.webchat.bean.TalkMessage;
import com.blackjade.webchat.util.SSDBClient;


@RestController
public class ChatController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SSDBClient sSDBClient;
	
    @Value("${push_url}")
    private  String pushUrl;
    @Value("${sign_url}")
    private  String signUrl;
	
	@RequestMapping(value = "/login")
	@ResponseBody
	public String login(HttpServletRequest request){
		
		String uname = request.getParameter("loginname");
		
		String result = sSDBClient.addUserToDB(uname);
		
		HttpSession session = request.getSession();
	    
	    session.setAttribute(session.getId(), uname);
		
		return result;
	}
	
	@RequestMapping(value = "/contacts")
	@ResponseBody
	public ResponseContact contacts(HttpServletRequest request){
		String type = request.getParameter("type");
		List<DataContact> data = new ArrayList<DataContact>();
		if("all".equals(type)){
			
			data = sSDBClient.getContacts("all_users",50);
			
		}
		
		if("recent".equals(type)){
			
			String faceName = request.getParameter("faceName");
			
			boolean flag = sSDBClient.userExist(faceName);
			
			if(flag == true){//在聊天框里，想要聊天的那个user
				DataContact dataContact = new DataContact();
				dataContact.setName(faceName);
				dataContact.setUnread(0);
				data.add(dataContact);
			}
			
			HttpSession session = request.getSession();
		    
		    String uid = (String) session.getAttribute(session.getId());
		    
		    int limit = 10;
//			String uid = request.getParameter("uid");
//			Integer limit = Integer.parseInt(request.getParameter("limit"));
			
			String key = sSDBClient.recent_contacts_key(uid);
			
			List<DataContact> recentData = sSDBClient.getContacts(key,limit);
			
			//删除跟之前重复的部分,专门为了购买商品时将对话的两个人在当前框内顶置
			for(DataContact d : recentData){
				if(d.getName().equals(faceName)){
					continue;
				}
				data.add(d);
			}
			
		}
		
		return new ResponseContact(0,"",data);
	}
	
	@RequestMapping(value = "/messages")
	@ResponseBody
	public ResponseMessage messages(HttpServletRequest request){
		
		String uid2 = request.getParameter("with");
		String sizeStr = request.getParameter("size");
		int size = 10;
		if(sizeStr == null || "".equals(sizeStr)){
			size = Integer.parseInt(sizeStr);
		}
		String max_msg_id = request.getParameter("max_msg_id");
		
		HttpSession session = request.getSession();
	    
	    String uid = (String) session.getAttribute(session.getId());
	    
	    List<Map> data = sSDBClient.listMessages(uid, uid2, size, max_msg_id);
		
	    return new ResponseMessage(0,"",data);
		
	}
	
	@RequestMapping(value = "/sign")
	@ResponseBody
    public String push(HttpServletRequest request){
		
		String cb = request.getParameter("cb");
		System.out.println("cb="+cb);
		String cname = request.getParameter("cname");
		System.out.println("cname="+cname);
		
//		String url = "http://192.168.236.129:8000/sign?cname="+cname+"&cb="+cb;
//		String url = "http://192.168.236.131:8000/sign?cname="+cname+"&cb="+cb;
//		String url = "http://192.168.1.9:8000/sign?cname="+cname+"&cb="+cb;
//		String url = "http://192.168.1.2:8000/sign?cname="+cname+"&cb="+cb;
		String url = signUrl + "?cname="+cname+"&cb="+cb;
		
        String u = "";
		try {
			u = restTemplate.getForObject(url, String.class );
		      System.out.println(u);
		      
		    HttpSession session = request.getSession();
		    
		    session.setAttribute(session.getId(), cname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
        return u;
    }
	

	@RequestMapping(value = "/send")
	@ResponseBody
    public ResponseObject send(HttpServletRequest request){
		String text = request.getParameter("text");
		System.out.println("content="+text);
		String cname = request.getParameter("uid2");
		System.out.println("cname="+cname);
        
        String u = "";
        ResponseObject responseObject = null;
		try {
		    HttpSession session = request.getSession();
		    
		    String from = (String) session.getAttribute(session.getId());
		    
//		    SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//		    
//		    Date ss = new Date();
//		    
//		    String time = format0.format(ss.getTime()); 
//		    
//		    String id = ss.getTime() + "";
//		    
//		    Data data = new Data(time,from,text,id);
//		    
//		    responseObject = new ResponseObject(0,"",data);
//		    
//		    String json = JSONObject.toJSONString(data);
		    
		    String chatKey = sSDBClient.chat_key(from,cname);
		    
		    TalkMessage talkMessage = sSDBClient.save_talk(chatKey,from,text);
		    
		    sSDBClient.save_record(from,cname,talkMessage.getTime());
		    
		    String pushMsg = sSDBClient.create_push_message(talkMessage);
		    
		    
//		    String url = "http://192.168.236.129:8000/push?";
//		    String url = "http://192.168.236.131:8000/push?";
//		    String url = "http://192.168.1.2:8000/push?";
//		    String url = "http://192.168.1.9:8000/push?";
		    String url = pushUrl;
		    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		    map.add("cname", cname);
		    map.add("content", pushMsg);
		       
		    u = restTemplate.postForObject(url, map, String.class);
		    
		    System.out.println(u);
		    
		    SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    
		    String time = format0.format(talkMessage.getTime()); 
		    
		    Data data = new Data(time,from,text,talkMessage.getId());
		    
		    responseObject = new ResponseObject(0,"",data);
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
        return responseObject;
    }
	
	@RequestMapping(value = "/upload")
	@ResponseBody
    public ResponseObject upload(HttpServletRequest request){
		String text = request.getParameter("text");
		System.out.println("content="+text);
		String cname = request.getParameter("uid2");
		System.out.println("cname="+cname);
        
        String u = "";
        ResponseObject responseObject = null;
		try {
		    HttpSession session = request.getSession();
		    
		    String from = (String) session.getAttribute(session.getId());
		    
		    String chatKey = sSDBClient.chat_key(from,cname);
		    
		    TalkMessage talkMessage = sSDBClient.save_talk(chatKey,from,text);
		    
		    sSDBClient.save_record(from,cname,talkMessage.getTime());
		    
		    String pushMsg = sSDBClient.create_push_message(talkMessage);
		    
		    String url = pushUrl;
		    
		    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		    map.add("cname", cname);
		    map.add("content", pushMsg);
		       
		    u = restTemplate.postForObject(url, map, String.class);
		    
		    System.out.println(u);
		    
		    SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    
		    String time = format0.format(talkMessage.getTime()); 
		    
		    Data data = new Data(time,from,text,talkMessage.getId());
		    
		    responseObject = new ResponseObject(0,"",data);
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
        return responseObject;
	}
}
