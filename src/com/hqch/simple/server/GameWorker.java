package com.hqch.simple.server;

import org.apache.log4j.Logger;

import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.io.RequestInfo;

public class GameWorker implements Runnable {

	private Logger logger = LoggerFactory.getLogger(GameWorker.class);
	
	private RequestInfo request;
	private ServiceManager serviceManager;
	
	public GameWorker(ServiceManager serviceManager, RequestInfo request){
		this.request = request;
		this.serviceManager = serviceManager;
	}
	
	@Override
	public void run() {
		try {
			serviceManager.executeService(request);
		} catch (Exception e) {
			logger.error("GameWorker", e);
		}
	}

}
