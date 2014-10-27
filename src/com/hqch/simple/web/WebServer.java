package com.hqch.simple.web;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.hqch.simple.container.Server;
import com.hqch.simple.log.LoggerFactory;

public class WebServer extends Server {
	
	private Logger logger = LoggerFactory.getLogger(WebServer.class);
	
	/**转发请求包含数据大小*/
	private static final int MSG_SIZE = 1048576;

	/** 处理转发请求连接池大小 */
	private static final int POOL_SIZE = 256;
	
	private HandlerManager handlerManager;
	
	
	public WebServer(){
		handlerManager = new HandlerManager();
	}
	
	public void init(){
		InetSocketAddress addr = new InetSocketAddress(port);// 需要监听的端口，即tcp连接建立的端口
		ServerSocketChannelFactory channelFactory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

		ExecutionHandler executionHandler = new ExecutionHandler(
				new OrderedMemoryAwareThreadPoolExecutor(POOL_SIZE, MSG_SIZE,
						MSG_SIZE));
		ChannelPipelineFactory pipelineFactory = new HttpServerPipelineFactory(executionHandler,handlerManager);
		
		bootstrap.setPipelineFactory(pipelineFactory);
		
		bootstrap.bind(addr);
		
		logger.info("web server was init.port:" + port);
	}
	
	@Override
	public void start() throws Exception {
		init();
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void registerHandler(String name, HttpHandler handler){
		handlerManager.registerHandler(name, handler);
	}
}
