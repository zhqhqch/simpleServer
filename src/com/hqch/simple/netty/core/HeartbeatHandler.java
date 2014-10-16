package com.hqch.simple.netty.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.util.Constants;
import com.hqch.simple.util.JavaSerializeUtils;

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
	
	private static ChannelBuffer buf;

	public HeartbeatHandler(String name) {
		this.name = name;
		buf = JavaSerializeUtils.getInstance().getChannelBuffer(Constants.HEARTBEAT);
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
