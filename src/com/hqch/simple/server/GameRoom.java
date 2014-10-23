package com.hqch.simple.server;

import com.hqch.simple.container.GameSession;
import com.hqch.simple.container.ScheduledTask;

public interface GameRoom {

	public String getRoomID();
	
	public void addSession(GameSession session);
	
	public void removeSession(String sessionID);
	
	public GameSession getGameSessionByID(String sessionID);
	
	public void broadcastMessage(String serviceID, Object message, String exceptSessionID);
	
	public void broadcastMessage(String serviceID, Object message);

	public void sendMessage(String serviceID, Object message, String sessionID);
	
	public void putAttribute(String key, Object value);
	
	public void removeAttribute(String key);
	
	public Object getAttributeByKey(String key);
	
	public void scheduleAtFixedRate(ScheduledTask task);
	
	public void destroy();
}
