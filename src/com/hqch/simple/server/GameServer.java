package com.hqch.simple.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import javax.script.ScriptException;

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
import com.hqch.simple.netty.core.JSONServerPipelineFactory;
import com.hqch.simple.netty.io.GameRequestThread;
import com.hqch.simple.netty.io.GameResponseThread;

public class GameServer extends Server {

	private Logger logger = LoggerFactory.getLogger(GameServer.class);
	
	private static final String PROTOCOL_JSON = "json";
	
	/**解析socket信息线程大小*/
	private static final int SERIALIZE_THREAD_SIZE = 10;
	
	/**转发请求包含数据大小*/
	private static final int MSG_SIZE = 1048576;

	/** 处理转发请求连接池大小 */
	private static final int POOL_SIZE = 256;

	private String protocol;
//	private Container container;
	private GameRequestThread requestThread;
	private GameResponseThread responseThread;
	private ServiceManager serviceManager;

	public GameServer() {
//		this.container = Container.get();
		
		this.serviceManager = new ServiceManager();
		this.responseThread = new GameResponseThread(SERIALIZE_THREAD_SIZE);
		this.requestThread = new GameRequestThread(SERIALIZE_THREAD_SIZE, 
				this.responseThread, this.serviceManager);
	}
	
	public void init() throws ScriptException {
		InetSocketAddress addr = new InetSocketAddress(port);// 需要监听的端口，即tcp连接建立的端口
		ServerSocketChannelFactory channelFactory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		DefaultChannelGroup allChannels = new DefaultChannelGroup(
				"accpetServerChannelGroup");

		ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

		ExecutionHandler executionHandler = new ExecutionHandler(
				new OrderedMemoryAwareThreadPoolExecutor(POOL_SIZE, MSG_SIZE,
						MSG_SIZE));
		ChannelPipelineFactory pipelineFactory = null;
		
		if(PROTOCOL_JSON.equalsIgnoreCase(protocol)){
			pipelineFactory = new JSONServerPipelineFactory(
					executionHandler, allChannels, requestThread);
		} else {
			throw new ScriptException("protocol was error.[" + protocol + "]");
		}
		
		bootstrap.setPipelineFactory(pipelineFactory);

		bootstrap.setOption("child.reuseAddress", true);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		
		Channel serverChannel = bootstrap.bind(addr);
		allChannels.add(serverChannel);
		
		
		logger.info("game server was init.port:" + port + ", protocol:" + protocol);
	}
	
	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		init();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public void registerService(GameService service) {
		serviceManager.registerService(service);
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
}
