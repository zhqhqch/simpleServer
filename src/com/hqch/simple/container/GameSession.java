package com.hqch.simple.container;

import org.jboss.netty.channel.Channel;

public interface GameSession {

	public String getSessionID();
	
	public boolean isConnected();
	
	public void sendMessage(String serviceID, Object message);
	
	public void lock();
	
	public void unlock();
	
	public void put(String key, Object value);
	
	public Object get(String key);
	
	public void remove(String key);
	
	public void heartbeat();

	public boolean check();

	public void request(Channel channel);
	
	public void invalidate();
	
	public boolean isRepeat();
}
