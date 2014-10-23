package com.hqch.simple.netty.io;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.Channel;

import com.hqch.simple.container.GameSession;

public class RequestInfo {

	private Channel channel;
	
	private String id;
	
	private long time;
	
	private String sn;
	
	private List<RequestParam> data;
	
	private GameSession session;
	
	public RequestInfo(){
		this.data = new ArrayList<RequestParam>();
	}
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	

	public List<RequestParam> getData() {
		return data;
	}

	public void setData(List<RequestParam> data) {
		this.data = data;
	}

	public GameSession getSession() {
		return session;
	}

	public void setSession(GameSession session) {
		this.session = session;
	}

	public void put(String key, Object value){
		this.data.add(new RequestParam(key, value));
	}
	
	public Object getParamByKey(String key){
		for(RequestParam param : data){
			if(param.getKey().equals(key)){
				return param.getObj();
			}
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return "RequestInfo [id=" + id + ", time=" + time + ", sn=" + sn
				+ ", data=" + data + "]";
	}
}
