package com.hqch.simple;

public class StringUtil {

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
