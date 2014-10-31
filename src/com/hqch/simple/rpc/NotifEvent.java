package com.hqch.simple.rpc;

import java.io.Serializable;

public class NotifEvent implements Serializable {

	private static final long serialVersionUID = -6352081125294468282L;

	private Object obj;
	
	private int type;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "NotifEvent [obj=" + obj + ", type=" + type + "]";
	}
	
}
