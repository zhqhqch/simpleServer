package com.hqch.simple.netty.core;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.execution.ExecutionHandler;

import com.hqch.simple.netty.io.GameRequestThread;

/**
 * netty 处理接受客户端消息
 * @author hqch
 *
 */
public class ProtobufServerPipelineFactory implements ChannelPipelineFactory {

	private final ExecutionHandler executionHandler;

	private DefaultChannelGroup channelGroup;

	private final ProtobufServerHandler protobufServerHandler;

	public ProtobufServerPipelineFactory(ExecutionHandler executionHandler,
			DefaultChannelGroup channelGroup, GameRequestThread requestThread) {
		this.channelGroup = channelGroup;
		this.executionHandler = executionHandler;
		this.protobufServerHandler = new ProtobufServerHandler(this.channelGroup, 
				requestThread);
	}

	@Override
	public final ChannelPipeline getPipeline() throws Exception {
		return Channels.pipeline(
				new ProtobufVarint32FrameDecoder(),
				new ProtobufEncoder(),
				executionHandler,
				protobufServerHandler);
	}
}
