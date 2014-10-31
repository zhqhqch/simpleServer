package com.hqch.simple.netty.core;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;

import com.hqch.simple.rpc.NotificationManager;
import com.hqch.simple.rpc.RPCServerWork;

/**
 * netty 处理接受客户端消息
 * @author hqch
 *
 */
public class RPCServerPipelineFactory implements ChannelPipelineFactory {

	private final ExecutionHandler executionHandler;

	private NotificationManager notificationManager;

	private final RPCServerHandler rpcServerHandler;

	public RPCServerPipelineFactory(ExecutionHandler executionHandler,
			NotificationManager notificationManager, RPCServerWork serverWork) {
		this.notificationManager = notificationManager;
		this.executionHandler = executionHandler;
		this.rpcServerHandler = new RPCServerHandler(this.notificationManager, 
				serverWork);
	}

	@Override
	public final ChannelPipeline getPipeline() throws Exception {
		return Channels.pipeline(
				new DefaultObjectFrameDecoder(),
				executionHandler,
				rpcServerHandler);
	}
}
