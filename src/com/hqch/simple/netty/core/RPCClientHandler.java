package com.hqch.simple.netty.core;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.netty.io.RPCResult;
import com.hqch.simple.rpc.RPCManager;


public class RPCClientHandler extends SimpleChannelHandler {

	private static Logger logger = LoggerFactory
			.getLogger(RPCClientHandler.class);

	private DefaultChannelGroup channelGroup;
	private String name;

	public RPCClientHandler(DefaultChannelGroup channelGroup, String name) {
		this.channelGroup = channelGroup;
		this.name = name;
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
		channelGroup.remove(ctx.getChannel());
		RPCManager.getInstance().getClientByName(name).disConnected();
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
		RPCResult result = (RPCResult)messageEvent.getMessage();
		RPCInfo rpc = RPCManager.getInstance().getRPC(result.getId());
		rpc.setException(result.getException());
		rpc.setRet(result.getObj());
		
		rpc.getLatch().countDown();
		
		logger.debug("received server messgae:" + result);
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
