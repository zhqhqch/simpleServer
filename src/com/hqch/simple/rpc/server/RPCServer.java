package com.hqch.simple.rpc.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.hqch.simple.container.Server;
import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.core.RPCServerPipelineFactory;
import com.hqch.simple.netty.io.RPCResponseThread;
import com.hqch.simple.rpc.RPCServerWork;

public class RPCServer extends Server {

	private Logger logger = LoggerFactory.getLogger(RPCServer.class);
	
	/**解析socket信息线程大小*/
	private static final int SERIALIZE_THREAD_SIZE = 10;
	
	/**转发请求包含数据大小*/
	private static final int MSG_SIZE = 1048576;

	/** 处理转发请求连接池大小 */
	private static final int POOL_SIZE = 256;
	
	private RPCResponseThread responseThread;
	private RPCServerWork serverWork;
	
	public RPCServer(){
		this.responseThread = new RPCResponseThread(SERIALIZE_THREAD_SIZE);
		this.serverWork = new RPCServerWork(responseThread);
	}
	
	private void init(){
		InetSocketAddress addr = new InetSocketAddress(port);// 需要监听的端口，即tcp连接建立的端口
		ServerSocketChannelFactory channelFactory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		DefaultChannelGroup allChannels = new DefaultChannelGroup(
				"rpcServerChannelGroup");

		ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

		ExecutionHandler executionHandler = new ExecutionHandler(
				new OrderedMemoryAwareThreadPoolExecutor(POOL_SIZE, MSG_SIZE,
						MSG_SIZE));
		ChannelPipelineFactory pipelineFactory = new RPCServerPipelineFactory(
				executionHandler, allChannels, serverWork);
		
		bootstrap.setPipelineFactory(pipelineFactory);

		bootstrap.setOption("child.reuseAddress", true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		
		Channel serverChannel = bootstrap.bind(addr);
		allChannels.add(serverChannel);
		
		logger.info("rpc server was init.port:" + port);
	}
	
	@Override
	public void start() throws Exception {
		init();
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
