package com.hqch.simple.rpc;

import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.hqch.simple.exception.BizException;
import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.netty.io.RPCRequest;
import com.hqch.simple.netty.io.RPCResponseThread;
import com.hqch.simple.netty.io.RPCResult;
import com.hqch.simple.util.ClassUtil;

public class RPCServerWork {

	private Logger logger = LoggerFactory.getLogger(RPCServerWork.class);
	
	/**处理RPC逻辑线程数*/
	private static int POOL_SIZE = 512;
	
	private LinkedBlockingQueue<RPCInfo> executeQueen = new LinkedBlockingQueue<RPCInfo>(
			512);
	
	private AtomicInteger threadCount = new AtomicInteger();
	
	private RPCResponseThread responseThread;
	
	private ScheduledThreadPoolExecutor scheduler;
	
	private TransactionInterceptor transactionInterceptor;
	
	public RPCServerWork(RPCResponseThread responseThread){
		this.transactionInterceptor = new TransactionInterceptor();
		this.responseThread = responseThread;
		
		scheduler = new ScheduledThreadPoolExecutor(POOL_SIZE, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r,"ResponseThread-"
						+ threadCount.incrementAndGet());
				return t;
			}
		});
	}
	
	
	private class WorkThread implements Runnable {
		@Override
		public void run() {
			try {
				RPCInfo info = executeQueen.poll(2, TimeUnit.SECONDS);
				execute(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void execute(RPCInfo info) throws BizException {
		RPCResult result = new RPCResult();
		try {
			RPCRequest request = info.getRpcRequest();
			String clazz = request.getTargetClass();
			Object obj = ClassUtil.getActionObject(clazz);
			Class<?>[] parameterTypes = ClassUtil.getParameterTypes(request.getParameterTypes());
			Object[] params = request.getParameters();
			Method method = obj.getClass().getMethod(request.getMethodName(), parameterTypes);
			info.setMethod(method);
			
			transactionInterceptor.beforeInvoke(info);
			
			Object objValue = method.invoke(obj, params);
			result.setObj(objValue);
			result.setId(request.getId());
			info.setResult(result);
			
			transactionInterceptor.afterInvoke(info);
		} catch (Exception ie) {
			result.setException(ie.getCause());
			transactionInterceptor.onError(info);
			logger.error("execute", ie);
		} finally {
			transactionInterceptor.endInvoke(info);
		}
		responseThread.send(info);
	}

	public void addWork(RPCInfo info) {
		try {
			executeQueen.offer(info, 2,TimeUnit.SECONDS);
			scheduler.execute(new WorkThread());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
