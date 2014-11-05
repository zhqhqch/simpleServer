package com.hqch.simple.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.hqch.simple.netty.io.GameResponseThread;
import com.hqch.simple.netty.io.ResponseInfo;
import com.hqch.simple.resource.cached.MemcachedResource;
import com.hqch.simple.server.GameServer;

public class GameSessionImpl implements GameSession {

	private static final int DISCONN_TIME = 1000 * 60;
	
	private static final int TOTAL_RECORD_TIMES = 10;
	
	private static final int MIN_RECORD_TIMES = 5;
	
	private static final int REQEUST_INTERVAL_SECOND = 3;
	
	private String sessionID;
	private Channel channel;
	private boolean connected;
	private boolean synchro;
	
	private Map<String, Object> data;
	
	private GameResponseThread responseThread;
	
	private MemcachedResource cached;
	
	private AtomicLong heartbeatCount;
	private long lastRequestTime;
	private long firstRequestTime;
	
	/**不公平锁*/
	private Lock lock = new ReentrantLock();
	
	private List<Long> requestTimeList;
	
	public GameSessionImpl(String sessionID, Channel channel,
			GameServer server){
		this.sessionID = sessionID;
		this.channel = channel;
		this.connected = true;
		this.responseThread = server.getGameResponseThread();
		this.synchro = server.isSynchroData();
		if(synchro){
			this.cached = Container.get().getMemcachedByName(server.getCachedName());
		}
		
		this.data = new HashMap<String, Object>();
		this.heartbeatCount = new AtomicLong(1);
		this.lastRequestTime = System.currentTimeMillis();
		this.firstRequestTime = lastRequestTime;
		this.requestTimeList =  Collections.synchronizedList(new ArrayList<Long>());
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
		ResponseInfo info = new ResponseInfo(channel);
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

	@Override
	public void put(String key, Object obj){
		if(synchro){
			cached.save(key, obj);
		} else {
			data.put(key, obj);
		}
	}
	
	@Override
	public void remove(String key){
		if(synchro){
			cached.remove(key);
		} else {
			data.remove(key);
		}
	}
	
	@Override
	public Object get(String key){
		Object retObj = null;
		if(synchro){
			retObj = cached.get(key);
		} else {
			retObj = data.get(key);
		}
		
		return retObj;
	}
	
	public void heartbeat(){
		heartbeatCount.incrementAndGet();
		this.lastRequestTime = System.currentTimeMillis();
	}

	@Override
	public boolean check() {
		if(System.currentTimeMillis() - lastRequestTime > DISCONN_TIME || !connected){
			return true;
		}
		return false;
	}

	@Override
	public void request(Channel channel) {
		if(this.channel.getId().intValue() != channel.getId().intValue()){
			ChannelFuture future = this.channel.getCloseFuture();
			future.addListener(ChannelFutureListener.CLOSE);
			this.channel = channel;
		}
		this.lastRequestTime = System.currentTimeMillis();
		
		if(this.requestTimeList.size() >= TOTAL_RECORD_TIMES){
			this.requestTimeList.remove(0);
		}
		this.requestTimeList.add(lastRequestTime);
	}

	@Override
	public void invalidate() {
		ChannelFuture future = this.channel.getCloseFuture();
		future.addListener(ChannelFutureListener.CLOSE);
		this.channel = null;
		
		this.connected = false;
	}
	
	public boolean isRepeat(){
		
		int size = this.requestTimeList.size();
		if(size <= MIN_RECORD_TIMES){
			return false;
		}
		
		long end = this.requestTimeList.get(size - 1);
		long interval = (end - firstRequestTime) / 1000;
		if(interval < REQEUST_INTERVAL_SECOND){
			return true;
		}
		
		long first = this.requestTimeList.get(0);
		interval = (end - first) / 1000;
		return interval < REQEUST_INTERVAL_SECOND;
	}

}
