package com.hqch.simple.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.netty.channel.Channel;

import com.hqch.simple.netty.io.GameResponseThread;
import com.hqch.simple.netty.io.ResponseInfo;

public class GameSessionImpl implements GameSession {

	private String sessionID;
	private Channel channel;
	private boolean connected;
	
	private Map<String, Object> data;
	
	private GameResponseThread responseThread;
	
	/**不公平锁*/
	private Lock lock = new ReentrantLock();
	
	public GameSessionImpl(String sessionID, Channel channel,
			GameResponseThread responseThread){
		this.sessionID = sessionID;
		this.channel = channel;
		this.connected = true;
		this.responseThread = responseThread;
		
		this.data = new HashMap<String, Object>();
	}
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public GameResponseThread getResponseThread() {
		return responseThread;
	}

	public void setResponseThread(GameResponseThread responseThread) {
		this.responseThread = responseThread;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	@Override
	public String getSessionID() {
		return this.sessionID;
	}

	@Override
	public boolean isConnected() {
		return this.connected;
	}

	@Override
	public void sendMessage(String serviceID, Object message) {
		ResponseInfo info = new ResponseInfo();
		info.setChannel(channel);
		info.setServiceID(serviceID);
		info.setData(message);
		
		responseThread.send(info);
	}

	@Override
	public void lock() {
		lock.lock();
	}

	@Override
	public void unlock() {
		lock.unlock();
	}

	
	public void put(String key, Object obj){
		data.put(key, obj);
	}
	
	public void remove(String key){
		data.remove(key);
	}
}
