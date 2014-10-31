package com.hqch.simple.netty.core;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.netty.io.RPCRequest;
import com.hqch.simple.rpc.NotificationManager;
import com.hqch.simple.rpc.RPCServerWork;

/**
 * 
 * @author hqch
 */
public class RPCServerHandler extends SimpleChannelHandler {

	private static Logger logger = LoggerFactory
			.getLogger(RPCServerHandler.class);

	private RPCServerWork serverWork;
	
	private NotificationManager notificationManager;
	
	public RPCServerHandler(NotificationManager notificationManager, 
			RPCServerWork serverWork) {
		this.notificationManager = notificationManager;
		this.serverWork = serverWork;
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
		notificationManager.removeChannel(ctx.getChannel());
	}

	@Override
	public void channelConnected(final ChannelHandlerContext ctx,
			final ChannelStateEvent e) throws Exception {
		notificationManager.addChannel(ctx.getChannel());
		ctx.sendUpstream(e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx,
			MessageEvent messageEvent) throws Exception {
		Object obj = messageEvent.getMessage();
		//rpc客户端发来的心跳包
		if(obj instanceof String){
			return;
		}
		RPCRequest info = (RPCRequest) obj;

		RPCInfo rpc = new RPCInfo();
		rpc.setId(info.getId());
		rpc.setChannel(ctx.getChannel());
		rpc.setRpcRequest(info);
		
		serverWork.addWork(rpc);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		if (e.getCause() instanceof ClosedChannelException) {
			e.getChannel().close();
		} else if (e.getCause() instanceof IOException) {
			e.getChannel().close();
		} else {
			logger.error("JSONServerHandler",e.getCause());
		}
	}
}
