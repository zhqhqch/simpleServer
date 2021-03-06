package com.hqch.simple.rpc.client;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.hqch.simple.exception.BizException;
import com.hqch.simple.exception.ConnectException;
import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.core.RPCClientPipelineFactory;
import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.netty.io.RPCRequestThread;
import com.hqch.simple.resource.Resource;
import com.hqch.simple.rpc.RPCManager;

public class RPCClient {

	private Logger logger = LoggerFactory.getLogger(RPCClient.class);
	
	/**请求RPC超时时间*/
	private static final long RPC_TIME_OUT = 30000;
	
	/**连接RPC超时时间*/
	private static final long CONNECT_TIME_OUT = 5000;
	
	/**解析socket信息线程大小*/
	private static final int SERIALIZE_THREAD_SIZE = 10;
	
	/** 转发请求包含数据大小 */
	private static final int MSG_SIZE = 1048576;

	/** 处理转发请求连接池大小 */
	private static final int POOL_SIZE = 100;
	
	private String name;
	
	private String host;
	
	private int port;
	
	private Channel channel;
	
	private boolean isConnected;
	
	private RPCRequestThread requestThread;
	
	private CountDownLatch latch;
	
	private ClientBootstrap bootstrap;
	
	private static ScheduledExecutorService service =
			Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t=new Thread(r, "InvokerNodeChecker");
					return t;
				}
			});
	
	
	
	public RPCClient(Resource res) {
		this.name = res.getName();
		this.host = res.getHost();
		this.port = res.getPort();
		
		this.requestThread = new RPCRequestThread(SERIALIZE_THREAD_SIZE);
		
		bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		DefaultChannelGroup allChannels = new DefaultChannelGroup(
				"rpcClientChannelGroup");
		ExecutionHandler executionHandler = new ExecutionHandler(
				new OrderedMemoryAwareThreadPoolExecutor(POOL_SIZE, MSG_SIZE,
						MSG_SIZE));
		ChannelPipelineFactory pipelineFactory = new RPCClientPipelineFactory(
				name, executionHandler, allChannels);
		bootstrap.setPipelineFactory(pipelineFactory);
		
		bootstrap.setOption("child.reuseAddress", true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		
		try {
			connect();
		} catch (BizException e) {
			logger.error(e.getMessage());
		}
		
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				checkAvailable();
			}
		}, 30, 30, TimeUnit.SECONDS);
	}

	private void checkAvailable(){
		try{
			if(!isConnected()){
				connect();
			}
		} catch (Throwable e) {
			for(int i = 0;i < 3; i++){
				try {
					connect();
					if(isConnected){
						break;
					}
				} catch (Exception e1) {
					logger.fatal("name:" + name + " unavailable." + e.getMessage());
				}
			}
		}
	}
	
	private void connect() throws BizException {
		this.latch = new CountDownLatch(1);
		// 创建无连接传输channel的辅助类(UDP),包括client和server
		ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
		
		checkFutureState(channelFuture);
		try {
			latch.await(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new ConnectException("can't connect to " + name);
		}
		if(!channelFuture.getChannel().isConnected()){
			throw new ConnectException("can't connect to " + name);
		}
	}
	
	public void checkFutureState(ChannelFuture channelFuture){
		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture connectFuture) throws Exception {
				if (connectFuture.isSuccess()) {
					channel = connectFuture.getChannel();
					isConnected = channel.isConnected();
					if(!isConnected){
						throw new ConnectException("can't connect to " + name);
					}
				}
				
				latch.countDown();
			}
		});
	}
	
	public boolean isConnected(){
		isConnected = channel != null ? channel.isConnected() : false;
		return isConnected;
	}
	
	public void disConnected() {
		isConnected = false;
	}
	
	public Object invoke(RPCInfo info) throws Throwable {
		if(!isConnected){
			connect();
		}
		
		info.setChannel(channel);
		
		requestThread.sendRequest(info);
			try {
				boolean timeOut = info.getLatch().await(RPC_TIME_OUT, TimeUnit.MILLISECONDS);
				if(!timeOut){
					long endTime = System.currentTimeMillis();
					long time = endTime - info.getStartTime();
					if(time > RPC_TIME_OUT && info.getRet() == null){
						throw new BizException("Request:"+ info.getTargetClass().getSimpleName() + "." + info.getMethodName()
								+ " timeout."+info.getStartTime()+"->"
								+ endTime+"/" + time);
					
					}
				}
					
			} catch (Exception e) {
				logger.error("invoke", e);
			}
		
		if (info.getException() != null) {
			throw info.getException();
		}
		RPCManager.getInstance().removeRPC(info.getId());
		return info.getRet();
	}

	public String getName(){
		return this.name;
	}
}
