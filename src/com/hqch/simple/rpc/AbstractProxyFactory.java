package com.hqch.simple.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.hqch.simple.log.LoggerFactory;

public abstract class AbstractProxyFactory {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<Class<?>,Object>  proxyActionMap;
	
	public AbstractProxyFactory(){
		proxyActionMap = new ConcurrentHashMap<Class<?>, Object>();
	}
	
	public abstract <T> T create(Class<T> actionInterface);
	
	protected boolean hasProxyObject(Class<?> actionInterface){
		return proxyActionMap.containsKey(actionInterface);
	}
	
	protected Object getProxyObject(Class<?> actionInterface){
		return proxyActionMap.get(actionInterface);
	}
	
	protected void addProxyObject(Class<?> actionInterface, Object obj){
		proxyActionMap.put(actionInterface, obj);
	}
	
	protected Object createProxy( Class<?> actionInterface,
			InvocationHandler theHandler){
		logger.info("create proxy:["+actionInterface+"] with handler:["+
				theHandler.getClass()+"]");
		Object proxy = Proxy.newProxyInstance(
				actionInterface.getClassLoader(),
				new Class[]{actionInterface},theHandler);
		return proxy;
	}
}
