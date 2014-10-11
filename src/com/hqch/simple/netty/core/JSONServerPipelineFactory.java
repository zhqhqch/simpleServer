package com.hqch.simple.netty.core;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.util.CharsetUtil;

import com.hqch.simple.netty.io.RequestThread;
import com.hqch.simple.netty.io.ResponseThread;

/**
 * netty 处理接受客户端消息
 * @author hqch
 *
 */
public class JSONServerPipelineFactory implements ChannelPipelineFactory {

	private final ExecutionHandler executionHandler;

	private DefaultChannelGroup channelGroup;

	private final JSONServerHandler jsonServerHandler;

	public JSONServerPipelineFactory(ExecutionHandler executionHandler,
			DefaultChannelGroup channelGroup, RequestThread requestThread) {
		this.channelGroup = channelGroup;
		this.executionHandler = executionHandler;
		this.jsonServerHandler = new JSONServerHandler(this.channelGroup, 
				requestThread);
	}

	@Override
	public final ChannelPipeline getPipeline() throws Exception {
		return Channels.pipeline(
				new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()),
				new StringDecoder(CharsetUtil.UTF_8), 
				new StringEncoder(CharsetUtil.UTF_8),
				executionHandler,
				jsonServerHandler);
	}
}
