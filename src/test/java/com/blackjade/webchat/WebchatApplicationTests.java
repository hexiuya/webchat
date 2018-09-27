package com.blackjade.webchat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.blackjade.webchat.bean.Data;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebchatApplicationTests {

	private RestTemplate restTemplate;  
	
	@Before
	public void setUp() throws Exception {
		restTemplate = new RestTemplate();
	}
	
	@Test
	public void junittest(){
		String url = "http://localhost:8883/push?msg=322323";

		String u = "";
		try {
			u = restTemplate.getForObject(url, String.class);
			System.out.println(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(u);
	}
	
	@Test
	public void contextLoads() {
		try {
			push();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sub() throws Exception {

		String url = "http://192.168.236.129:8100/sub?cname=12&seq=1";

		String u = "";
		try {
			u = restTemplate.getForObject(url, String.class);
			System.out.println(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(u);

	}
	
	
	public void push() throws Exception {

		String url = "http://192.168.236.129:8000/push?cname=12&content=hi";

		String u = "";
		try {
			u = restTemplate.getForObject(url, String.class);
			System.out.println(u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(u);

	}
	
	@Test
	public void push2(){
		
		String url = "http://192.168.236.129:8000/push";
		
		Data data = new Data("322332","222","hello","322332");
		
		String json = JSONObject.toJSONString(data);
		
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
	       map.add("cname", "333");
	       map.add("content", json);
	       
	       String result = restTemplate.postForObject(url, map, String.class);
	       
	       System.out.println(result);
	}
	
	
	@Test
	public void login(){
		
		String url = "http://192.168.236.129:8000/push";
		
		Data data = new Data("322332","222","hello","322332");
		
		String json = JSONObject.toJSONString(data);
		
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
	       map.add("cname", "333");
	       map.add("content", json);
	       
	       String result = restTemplate.postForObject(url, map, String.class);
	       
	       System.out.println(result);
	}
}
