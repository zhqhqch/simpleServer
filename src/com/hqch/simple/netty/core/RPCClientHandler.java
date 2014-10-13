package com.hqch.simple.netty.core;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.Arrays;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.io.RPCRequest;
import com.hqch.simple.netty.io.RPCResult;
import com.hqch.simple.rpc.RPCManager;


public class RPCClientHandler extends SimpleChannelHandler {

	private static Logger logger = LoggerFactory
			.getLogger(RPCClientHandler.class);

	private DefaultChannelGroup channelGroup;

	public RPCClientHandler(DefaultChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
		channelGroup.remove(ctx.getChannel());
	}

	@Override
	public void channelConnected(final ChannelHandlerContext ctx,
			final ChannelStateEvent e) throws Exception {
		channelGroup.add(ctx.getChannel());
		ctx.sendUpstream(e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx,
			MessageEvent messageEvent) throws Exception {
		String msg = (String)messageEvent.getMessage();
		JSONObject request = JSONObject.fromObject(msg);
		RPCRequest info = (RPCRequest)JSONObject.toBean(request, RPCResult.class);
		logger.debug("received server messgae:" + info);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		if (e.getCause() instanceof ClosedChannelException) {
			e.getChannel().close();
		} else if (e.getCause() instanceof IOException) {
			e.getChannel().close();
		} else {
			logger.error("RPCClientHandler",e.getCause());
		}
	}

}
