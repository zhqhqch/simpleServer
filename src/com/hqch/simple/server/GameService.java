package com.hqch.simple.server;

public interface GameService {

	public void beforeService(ServiceContext context);
	
	public void afterService(ServiceContext context);
	
	public void onError(ServiceContext context, Throwable e);
}
