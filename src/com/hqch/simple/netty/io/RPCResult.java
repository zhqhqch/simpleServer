package com.hqch.simple.netty.io;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class RPCResult implements Serializable {

	private static final long serialVersionUID = 6115313555381829236L;

	private String id;
	
	private Object obj;
	
	private Throwable exception;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	@Override
	public String toString() {
		return JSONObject.fromObject(this) + "\n";
	}
	
}
