package com.hqch.simple.core.script;

import com.hqch.simple.container.Container;
import com.hqch.simple.container.Server;
import com.hqch.simple.exception.BizException;
import com.hqch.simple.resource.Resource;

public class StartupContextImpl implements StartupContext {
	
	private Container container;
	
	public StartupContextImpl(Container c){
		this.container = c;
	}
	
	@Override
	public void registerCache(String name, Resource res) {
		container.registerCache(name, res);
	}
	
	@Override
	public void addRemoteServer(Resource res) {
		container.addRemoteServer(res);
	}
	
	@Override
	public void printLog(String msg) {
		System.out.println(msg);
	}
	
	@Override
	public void initServer(Server server) throws BizException {
		container.initServer(server);
	}
	
}
