package com.hqch.simple.util;

import java.util.UUID;

public class StringUtil {

	public static String generateID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static boolean isNull(String str){
		if(str == null){
			return true;
		}
		if(str.trim().length() == 0){
			return true;
		}
		if(str.trim().equalsIgnoreCase("null")){
			return true;
		}
		
		return false;
	}
}
