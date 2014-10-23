package com.hqch.simple.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.hqch.simple.container.Container;
import com.hqch.simple.container.GameSession;
import com.hqch.simple.container.ScheduledTask;

public class GameRoomImpl implements GameRoom {

	private String id;
	private Map<String, GameSession> sessiomMap;
	private Map<String, Object> attributeMap;
	private ScheduledThreadPoolExecutor service;
	
	public GameRoomImpl(String id){
		this.id = id;
		this.sessiomMap = new ConcurrentHashMap<String, GameSession>();
		this.attributeMap = new ConcurrentHashMap<String, Object>();
		this.service = Container.get().getGameThreadPool();
	}
	
	@Override
	public String getRoomID(){
		return this.id;
	}
	
	@Override
	public void addSession(GameSession session) {
		this.sessiomMap.put(session.getSessionID(), session);
	}

	@Override
	public void removeSession(String sessionID) {
		this.sessiomMap.remove(sessionID);
	}

	@Override
	public GameSession getGameSessionByID(String sessionID) {
		return sessiomMap.get(sessionID);
	}

	@Override
	public void broadcastMessage(String serviceID, Object message,
			String exceptSessionID) {
		if(this.sessiomMap.size() > 0){
			for(GameSession session : this.sessiomMap.values()){
				if(!session.getSessionID().equals(exceptSessionID)){
					session.sendMessage(serviceID, message);
				}
			}
		}
	}

	@Override
	public void broadcastMessage(String serviceID, Object message) {
		if(this.sessiomMap.size() > 0){
			for(GameSession session : this.sessiomMap.values()){
				session.sendMessage(serviceID, message);
			}
		}
	}

	@Override
	public void sendMessage(String serviceID, Object message, String sessionID) {
		GameSession session = getGameSessionByID(sessionID);
		if(session != null){
			session.sendMessage(serviceID, message);
		}
	}

	@Override
	public void putAttribute(String key, Object value) {
		this.attributeMap.put(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		this.attributeMap.remove(key);
	}

	@Override
	public Object getAttributeByKey(String key) {
		return this.attributeMap.get(key);
	}

	@Override
	public void destroy() {
		this.attributeMap.clear();
		this.sessiomMap.clear();
	}

	@Override
	public void scheduleAtFixedRate(ScheduledTask task) {
		ScheduledFuture<?> sf = service.scheduleAtFixedRate(task,
				task.getInitialDelay(), task.getRepeat(), task.getTimeUnit());
		task.setScheduledFuture(sf);
	}
}
