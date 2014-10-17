package com.hqch.simple.netty.io;

import java.io.Serializable;

public class RequestParam implements Serializable {

	private static final long serialVersionUID = -6711481828731646289L;

	private String key;
	
	private Object obj;
	
	public RequestParam(){}
	
	public RequestParam(String key, Object obj){
		this.key = key;
		this.obj = obj;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "RequestParam [key=" + key + ", obj=" + obj + "]";
	}
}
