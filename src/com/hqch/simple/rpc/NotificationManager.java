package com.hqch.simple.rpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.io.RPCResult;
import com.hqch.simple.util.Constants;
import com.hqch.simple.util.JavaSerializeUtils;

public class NotificationManager {

	private Logger logger = LoggerFactory.getLogger(NotificationManager.class);
	
	private List<Channel> clientList;
	
	private LinkedBlockingQueue<NotifEvent> notifQueue;
	
	private AtomicInteger count = new AtomicInteger();
	
	private ThreadPoolExecutor threadPool;
	
	private static final int DEFAULT_CORE_POOL_SIZE = 128;
	private static final int DEFAULT_MAX_POOL_SIZE = 256;
	private static final int MAX_REQUEST_QUEUE_SIZE = 512;
	
	private ArrayBlockingQueue<Runnable> requestQueue;
	
	public NotificationManager(){
		clientList = Collections.synchronizedList(new ArrayList<Channel>());
		notifQueue = new LinkedBlockingQueue<NotifEvent>();
		
		ThreadFactory tf=new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t=new Thread(r);
				t.setName("NotifThread-" + count.incrementAndGet());
				return t;
			}
		};
		
		this.requestQueue = new ArrayBlockingQueue<Runnable>(MAX_REQUEST_QUEUE_SIZE);
		
		this.threadPool = new ThreadPoolExecutor(
				DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE,
				60L,
				TimeUnit.SECONDS, 
				requestQueue, tf);
		this.threadPool.setRejectedExecutionHandler(
				new ThreadPoolExecutor.AbortPolicy());
		
		
		start();
	}
	
	public void addChannel(Channel channel){
		clientList.add(channel);
	}
	
	public void removeChannel(Channel channel){
		clientList.remove(channel);
	}
	
	public void sendNotif(NotifEvent event){
		try {
			boolean success = notifQueue.offer(event, 2, TimeUnit.SECONDS);
			if(!success){
				logger.error("sendNotif error:" + event);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("sendNotif error:" + event);
		}
	}
	
	public void start(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					while(true){
						NotifEvent event = notifQueue.poll(2, TimeUnit.SECONDS);
						if(event != null){
							threadPool.execute(new NotifThread(clientList, event));
						}
					}
				} catch (Exception e){
					e.printStackTrace();
					logger.error("send notif error", e);
				}
			}
		}).start();
	}
	
	
	private class NotifThread implements Runnable {
		private List<Channel> channelList;
		private NotifEvent event;
		
		public NotifThread(List<Channel> channelList, NotifEvent event){
			this.channelList = channelList;
			this.event = event;
		}
		
		@Override
		public void run() {
			RPCResult result = new RPCResult();
			result.setId(Constants.NOTIF_TAG);
			result.setObj(event);
			for(Channel channel : channelList){
				channel.write(JavaSerializeUtils.getInstance().getChannelBuffer(result));
			}
		}
	}
}
