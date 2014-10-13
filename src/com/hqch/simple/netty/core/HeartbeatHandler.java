package com.hqch.simple.netty.core;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

import com.hqch.simple.log.LoggerFactory;

/**
 * 与目标服务器心跳包
 * 
 * @author hqch
 * 
 */
public class HeartbeatHandler extends IdleStateAwareChannelHandler {

	private static Logger logger = LoggerFactory
			.getLogger(HeartbeatHandler.class);
	
	private final String name;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static byte[] heartbeatMsg;
	private static ChannelBuffer buf;

	public HeartbeatHandler(String name) {
		this.name = name;
		try {
			heartbeatMsg = "heartbeat\n".getBytes("UTF-8");
			buf = ChannelBuffers.copiedBuffer(heartbeatMsg);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)
			throws Exception {

		logger.debug(name
				+ "'s heartbeat:"
				+ sdf.format(new Date()));
		e.getChannel().write(buf);
	}
}
