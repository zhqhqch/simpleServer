package com.hqch.simple.web;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public class HandlerManager {

	private final static int POOL_SIZE = 256;
	
	private Map<String, HttpHandler> handlerMap;
	
	private ScheduledThreadPoolExecutor scheduler;
	private AtomicInteger threadCount;
	
	public HandlerManager(){
		handlerMap = new HashMap<String, HttpHandler>();
		threadCount = new AtomicInteger();
		
		scheduler = new ScheduledThreadPoolExecutor(POOL_SIZE, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r,"WebThread-"
						+ threadCount.incrementAndGet());
				return t;
			}
		});
	}
	
	public void registerHandler(String name, HttpHandler handler) {
		handlerMap.put(name, handler);
	}
	
	public void execute(HttpMessage request){
		String url = request.getUrl();
		String[] path = url.split("/");
		
		if(path.length != 2){
			request.error(HttpResponseStatus.INTERNAL_SERVER_ERROR, "request error:" + url);
			return;
		}
		
		HttpHandler handler = handlerMap.get(path[0]);
		if(handler == null){
			request.error(HttpResponseStatus.NOT_FOUND, "can't found servlet:" + url);
			return;
		}
		
		scheduler.execute(new WebWorkThread(handler,request));
	}
}
