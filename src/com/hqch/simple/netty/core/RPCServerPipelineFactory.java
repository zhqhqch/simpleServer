package com.hqch.simple.netty.core;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.handler.execution.ExecutionHandler;

import com.hqch.simple.rpc.RPCServerWork;

/**
 * netty 处理接受客户端消息
 * @author hqch
 *
 */
public class RPCServerPipelineFactory implements ChannelPipelineFactory {

	private final ExecutionHandler executionHandler;

	private DefaultChannelGroup channelGroup;

	private final RPCServerHandler rpcServerHandler;

	public RPCServerPipelineFactory(ExecutionHandler executionHandler,
			DefaultChannelGroup channelGroup, RPCServerWork serverWork) {
		this.channelGroup = channelGroup;
		this.executionHandler = executionHandler;
		this.rpcServerHandler = new RPCServerHandler(this.channelGroup, 
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
