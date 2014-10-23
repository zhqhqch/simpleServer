package com.hqch.simple.netty.io;

import net.sf.json.JSONObject;

import org.jboss.netty.channel.Channel;

public class ResponseInfo extends BaseResponse {

	private static final long serialVersionUID = -6463538616879770842L;

	private Channel channel;
	
	private String serviceID;
	
	private Object data;
	
	public ResponseInfo(Channel channel){
		this.channel = channel;
	}

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
	
	//通信使用String类型，发送内容后必须加\n
	public void write(){
		channel.write(JSONObject.fromObject(this).toString() + "\n");
	}

	@Override
	public String toString() {
		return "ResponseInfo [serviceID=" + serviceID + ", data=" + data + "]";
	}

	@Override
	public String toJSONString() {
		return JSONObject.fromObject(this).toString() + "\n";
	}

	@Override
	public Object toProtobuf() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
