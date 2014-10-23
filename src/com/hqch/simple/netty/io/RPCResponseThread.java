package com.hqch.simple.netty.io;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.hqch.simple.log.LoggerFactory;

public class RPCResponseThread {

	private static Logger logger = LoggerFactory
			.getLogger(RPCResponseThread.class);
	
	private LinkedBlockingQueue<RPCInfo> sendQueen = new LinkedBlockingQueue<RPCInfo>(
			512);
	
	private ScheduledThreadPoolExecutor scheduler;

	private AtomicInteger threadCount = new AtomicInteger();
	
	private AtomicInteger reStartThreadCount;
	
	private int poolSize;
	
	public RPCResponseThread(int poolSize){
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
	
	
	private class WorkThread implements Runnable {
		@Override
		public void run() {
			while(true){
				waitForProcess();
			}
		}
		
		private void waitForProcess() {
			try {
				RPCInfo info = sendQueen.poll(2, TimeUnit.SECONDS);
				if(info != null){
					info.sendResult();
				}
			} catch (Exception e) {
				logger.error("waitForProcessMessage",e);
			}
		}
	}


	public void send(RPCInfo info) {
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
			
		}
	}
}
