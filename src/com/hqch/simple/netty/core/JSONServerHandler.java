package com.hqch.simple.netty.core;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.hqch.simple.container.Container;
import com.hqch.simple.container.GameSession;
import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.io.GameRequestThread;
import com.hqch.simple.netty.io.RequestInfo;
import com.hqch.simple.netty.io.RequestParam;
import com.hqch.simple.util.Constants;

/**
 * 
 * 
 * @author hqch
 * 
 */
public class JSONServerHandler extends SimpleChannelHandler {

	private static Logger logger = LoggerFactory
			.getLogger(JSONServerHandler.class);

	private DefaultChannelGroup channelGroup;
	
	private GameRequestThread requestThread;
	
	private Map<String, Class<?>> classMap;
	
	private Container container;
	
	public JSONServerHandler(DefaultChannelGroup channelGroup, 
			GameRequestThread requestThread) {
		this.channelGroup = channelGroup;
		this.requestThread = requestThread;
		this.classMap = new HashMap<String, Class<?>>();
		this.classMap.put("data", RequestParam.class);
		this.container = Container.get();
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
		channelGroup.remove(ctx.getChannel());
		
//		int channelID = ctx.getChannel().getId();
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
		RequestInfo info = (RequestInfo)JSONObject.toBean(request, RequestInfo.class, classMap);
		info.setChannel(ctx.getChannel());
		
		//客户端发送的心跳包
		if(Constants.HEARTBEAT.equals(info.getSn())){
			heartbeat(info.getId());
			return;
		}
		
		requestThread.accept(info);
	}
	
	private void heartbeat(String sessionID){
		GameSession session = container.getGameSessionByID(sessionID);
		if(session != null){
			session.heartbeat();
		}
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
