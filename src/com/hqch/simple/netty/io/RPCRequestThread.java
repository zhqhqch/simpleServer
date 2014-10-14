package com.hqch.simple.netty.io;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.rpc.RPCWorker;

public class RPCRequestThread {

	private static Logger logger = LoggerFactory
			.getLogger(GameRequestThread.class);
	
	private static final int DEFAULT_CORE_POOL_SIZE = 256;
	private static final int DEFAULT_MAX_POOL_SIZE = 512;
	private static final int MAX_REQUEST_QUEUE_SIZE = 1024;
	
	private LinkedBlockingQueue<RPCInfo> receivedQueen = new LinkedBlockingQueue<RPCInfo>(
			MAX_REQUEST_QUEUE_SIZE);
	
	private ArrayBlockingQueue<Runnable> requestQueue;
	
	private ScheduledThreadPoolExecutor scheduler;

	private AtomicInteger reStartThreadCount;
	
	private int poolSize;
	
	private AtomicInteger count = new AtomicInteger();
	
	private AtomicInteger workCount = new AtomicInteger();
	
	
	private ThreadPoolExecutor threadPool;
	
	public RPCRequestThread(int poolSize){
		this.poolSize = poolSize;
		this.reStartThreadCount = new AtomicInteger(1);
		
		ThreadFactory tf=new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("WorkerThread-" + workCount.incrementAndGet());
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
		
		scheduler = new ScheduledThreadPoolExecutor(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r,"RequestThread-"
						+ count.incrementAndGet());
				return t;
			}
		});
		
		scheduler.execute(new WorkThread());
	}
	
	private class WorkThread implements Runnable {
		@Override
		public void run() {
			while(true){
				RPCInfo info = waitForProcess();
				logger.debug(receivedQueen.size() + "-->received clinet message:" + info);
				
				if(info != null){
					RPCWorker gw = new RPCWorker(info);
					threadPool.execute(gw);
					
					count.getAndIncrement();
					System.out.println(count.get());
				}
			}
		}
		
		private RPCInfo waitForProcess() {
			RPCInfo info = null;
			try {
				info = receivedQueen.poll(2, TimeUnit.SECONDS);
				logger.debug(receivedQueen.size() + "@@@@@@@@@@" + info);
			} catch (InterruptedException e) {
				logger.error("waitForProcessMessage",e);
			}
			
			return info;
		}
	}
	
	
	public RPCResult sendRequest(RPCInfo info){
		try {
			boolean success = receivedQueen.offer(info, 2,
					TimeUnit.SECONDS);
			while (false == success) {
				// maybe PushRecvThread is break,restart the thread again
				if (reStartThreadCount.get() < poolSize) {
					scheduler.execute(new WorkThread());
					reStartThreadCount.getAndIncrement();
				}
				success = receivedQueen.offer(info, 2,
						TimeUnit.SECONDS);
			}
		} catch (InterruptedException e) {
			logger.error("addSocketMessage",e);
		}
		
		logger.debug("client msg len:" + receivedQueen.size());
		
		RPCResult result = new RPCResult();
		result.setId(info.getId());
		return result;
	}
}
