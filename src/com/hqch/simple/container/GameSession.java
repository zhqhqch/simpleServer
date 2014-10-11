package com.hqch.simple.container;

public interface GameSession {

	public String getSessionID();
	
	public boolean isConnected();
	
	public void sendMessage(String serviceID, Object message);
	
	public void lock();
	
	public void unlock();
}
