package com.hqch.simple.netty.core;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;


public class RPCClientPipelineFactory implements ChannelPipelineFactory {

	private final ExecutionHandler executionHandler;

	private DefaultChannelGroup channelGroup;

	private final RPCClientHandler rpcClientHandler;

	private final IdleStateHandler idleStateHandler;
	
	private final HeartbeatHandler heartbeatHandler;
	
	public RPCClientPipelineFactory(String name, ExecutionHandler executionHandler,
			DefaultChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
		this.executionHandler = executionHandler;
		this.rpcClientHandler = new RPCClientHandler(this.channelGroup,name);
		Timer timer = new HashedWheelTimer();
		this.idleStateHandler = new IdleStateHandler(timer, 60, 60,0);
		this.heartbeatHandler = new HeartbeatHandler(name);
	}

	@Override
	public final ChannelPipeline getPipeline() throws Exception {
		return Channels.pipeline(
				new DefaultObjectFrameDecoder(),
				executionHandler,
				rpcClientHandler,
				idleStateHandler,
				heartbeatHandler);
	}
}
