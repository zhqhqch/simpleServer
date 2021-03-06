package com.hqch.simple.web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import com.hqch.simple.log.LoggerFactory;

public class WebWorkThread implements Runnable {

	private Logger logger = LoggerFactory.getLogger(WebWorkThread.class);
	
	private HttpHandler handler;
	
	private HttpMessage request;
	private String url;
	private String methodName;
	
	public WebWorkThread(HttpHandler handler, HttpMessage request){
		this.handler = handler;
		this.request = request;
		this.url = request.getUrl();
		this.methodName = url.split("/")[1];
	}
	
	
	@Override
	public void run() {
		try {
			Method method = handler.getClass().getMethod(methodName, HttpMessage.class);
			method.invoke(handler, request);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			request.error(HttpResponseStatus.NOT_FOUND, "can't found servlet:" + url);
			logger.warn("can't found servlet:" + url);
			return;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
