package com.hqch.simple.netty.core;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.hqch.simple.exception.BizException;
import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.util.Constants;
import com.hqch.simple.util.JavaSerializeUtils;

public class DefaultObjectFrameDecoder extends FrameDecoder {

	private static Logger logger = LoggerFactory
			.getLogger(DefaultObjectFrameDecoder.class);
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		try {
			if (!buffer.readable()) {
				return null;
			}
			int index = buffer.readerIndex();

			short head = buffer.getShort(index);
			if (head != Constants.HEAD) {
				throw new BizException("message head value is error!"
						+ channel.getRemoteAddress());
			}
			int len = buffer.getInt(index + 2);
			if (buffer.readableBytes() < len) {
				return null;
			}
			//
			ByteBuffer byteButter = ByteBuffer.allocate(len - 8);
			buffer.getBytes(index + 6, byteButter);

			short last = buffer.getShort(index + len - 2);
			if (last != Constants.END) {
				throw new BizException(
						"decodeServerRequest message last value is error!");
			}

			buffer.skipBytes(len);
			Object obj = JavaSerializeUtils.getInstance().deserialize(
					byteButter);
			return obj;
		} catch (Exception e) {
			logger.error("Exception message : ", e);
		}
		return null;
	}

}
