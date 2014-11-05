package com.hqch.simple.web;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class HttpMessage {

	private String url;
	
	private StringBuilder content;

	private Map<String,Object> requestParams;
	
	private Channel channel;
	
	public HttpMessage(String url) {
		this.url = url;
		this.content = new StringBuilder();
		this.requestParams = new HashMap<String, Object>();
	}

	public void setChannel(Channel channel){
		this.channel = channel;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public StringBuilder getContent() {
		return content;
	}

	public void setContent(StringBuilder content) {
		this.content = content;
	}

	public Map<String, Object> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, Object> requestParams) {
		this.requestParams = requestParams;
	}

	public void back(String msg){
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK);
		ChannelFuture future = null;
		try {
			long length = msg.length();
			response.setHeader(HttpHeaders.Names.CONTENT_LENGTH,
					String.valueOf(length));
			response.setHeader(HttpHeaders.Names.CONTENT_TYPE,
					"text/html; charset=UTF-8");
			ChannelBuffer buffer = new DynamicChannelBuffer(2048);
			buffer.writeBytes(msg.getBytes("UTF-8"));
			response.setContent(buffer);
			future = channel.write(response);
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	public void error(HttpResponseStatus status, String msg){
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				status);
		ChannelFuture future = null;
		try {
			long length = msg.length();
			response.setHeader(HttpHeaders.Names.CONTENT_LENGTH,
					String.valueOf(length));
			response.setHeader(HttpHeaders.Names.CONTENT_TYPE,
					"text/html; charset=UTF-8");
			ChannelBuffer buffer = new DynamicChannelBuffer(2048);
			buffer.writeBytes(msg.getBytes("UTF-8"));
			response.setContent(buffer);
			future = channel.write(response);
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			future.addListener(ChannelFutureListener.CLOSE);
		}
}
	
	@Override
	public String toString() {
		return "HttpMessage [url=" + url + ", content=" + content + "]";
	}
}
