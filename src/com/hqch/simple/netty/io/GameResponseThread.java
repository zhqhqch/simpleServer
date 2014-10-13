package com.hqch.simple.netty.io;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.hqch.simple.log.LoggerFactory;

public class GameResponseThread {

	private static Logger logger = LoggerFactory
			.getLogger(GameResponseThread.class);
	
	private LinkedBlockingQueue<ResponseInfo> sendQueen = new LinkedBlockingQueue<ResponseInfo>(
			512);
	
	private ScheduledThreadPoolExecutor scheduler;

	private AtomicInteger threadCount = new AtomicInteger();
	
	private AtomicInteger reStartThreadCount;
	
	private int poolSize;
	
	public GameResponseThread(int poolSize){
		this.poolSize = poolSize;
		this.reStartThreadCount = new AtomicInteger(1);
		
		scheduler = new ScheduledThreadPoolExecutor(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r,"ResponseThread-"
						+ threadCount.incrementAndGet());
				return t;
			}
		});
		scheduler.execute(new WorkThread());
	}
	
	public void send(ResponseInfo info){
		if (info != null) {
			try {
				boolean success = sendQueen.offer(info, 2,
						TimeUnit.SECONDS);
				if (false == success) {
					// maybe PushRecvThread is break,restart the thread again
					if (reStartThreadCount.get() < poolSize) {
						scheduler.execute(new WorkThread());
						reStartThreadCount.getAndIncrement();
					}
				}
			} catch (InterruptedException e) {
				logger.error("addSocketMessage",e);
			}
			
			logger.debug("send msg len:" + sendQueen.size());
		}
	}
	
	
	private class WorkThread implements Runnable {
		@Override
		public void run() {
			while(true){
				waitForProcess();
			}
		}
		
		private void waitForProcess() {
			try {
				ResponseInfo info = sendQueen.poll(2, TimeUnit.SECONDS);
				if(info != null){
					info.write();
					logger.debug(sendQueen.size() + "-->send clinet message:" + info);
				}
			} catch (Exception e) {
				logger.error("waitForProcessMessage",e);
			}
		}
	}
}
