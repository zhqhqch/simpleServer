package com.hqch.simple.netty.io;

import net.sf.json.JSONObject;

import org.jboss.netty.channel.Channel;

public class ResponseInfo {

	private Channel channel;
	
	private String serviceID;
	
	private Object data;

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	//通信使用String类型，发送内容后必须加\n
	public void write(){
		channel.write(JSONObject.fromObject(this).toString() + "\n");
	}

	@Override
	public String toString() {
		return "ResponseInfo [serviceID=" + serviceID + ", data=" + data + "]";
	}
	
}
