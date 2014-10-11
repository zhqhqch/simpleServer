package com.hqch.simple.server;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hqch.simple.exception.ServiceException;
import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.io.RequestInfo;

public class ServiceManager {
	private Logger logger = LoggerFactory.getLogger(ServiceManager.class);
	
	private Map<String,GameService> serviceMap;
	private Map<String,Method> serviceMethodMap;
	
	public ServiceManager(){
		this.serviceMap = new HashMap<String, GameService>();
		this.serviceMethodMap = new HashMap<String, Method>();
	}
	
	public void registerService(GameService service){
		String name = service.getClass().getSimpleName();
		if(serviceMap.containsKey(name)){
			throw new IllegalArgumentException("service with name:"+name
					+" already exists.");
		}
		serviceMap.put(name,service);
		registerServiceMethod(service.getClass());
	}
	
	private void registerServiceMethod(Class<?> serviceClass){
		Method allMethod[] = serviceClass.getDeclaredMethods();
		for(Method method : allMethod){
			if(!Modifier.isPublic(method.getModifiers())){
				continue;
			}
			Class<?> parameterTypes[] = method.getParameterTypes();
			if(parameterTypes == null || parameterTypes.length<1){
				continue;
			}
			if(!parameterTypes[0].equals(ServiceContext.class)){
				continue;
			}
			if(method.getName().equals("beforeService")
					||method.getName().equals("afterService")){
				continue;
			}

			String serviceName = serviceClass.getSimpleName()+"."+method.getName();
			serviceMethodMap.put(serviceName, method);
		}
	}
	
	/**
	 * 执行指定的接口
	 * @param request
	 */
	public void executeService(RequestInfo request)  throws Exception {
		String serviceID = request.getSn();
		Method method = serviceMethodMap.get(serviceID);
		
		if(method == null){
			logger.error("can't found service:"+serviceID);
			throw new ServiceException("can't found service:" + serviceID);
		}
		
		GameService service = getServiceInstance(serviceID);
		
		ServiceContext context = new ServiceContextImpl(request);
		
		service.beforeService(context);
		
		if(method.isAnnotationPresent(Synchronized.class)){
			try{
				request.getSession().lock();
				invokeService(method, service, context);
			} finally {
				request.getSession().unlock();
			}
		} else {
			invokeService(method, service, context);
		}
		
		service.afterService(context);
	}
	
	private void invokeService(Method method, GameService service, ServiceContext context) throws Exception {
		method.invoke(service,context);
	}
	
	private GameService getServiceInstance(String serviceID) throws InstantiationException, IllegalAccessException{
		GameService service = serviceMap.get(
				serviceID.substring(0,serviceID.indexOf('.')));
		return service;
	}
}
