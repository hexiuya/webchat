package com.blackjade.webchat;

import java.util.Arrays;

import org.junit.Test;

public class ArrayTest {

	@Test
	public void ArraySortTest(){
		String[] arr = {"Zill","AllenWalker","Boy","galley","cill","Cilk","cill"};
		Arrays.sort(arr);
	    System.out.println(Arrays.toString(arr));
	    StringBuilder sb = new StringBuilder("chat|");
	    for(String str : arr){
	    	sb.append(str);
	    }
	    
	    System.out.println(sb.toString());
	}
}
