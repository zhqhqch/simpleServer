package com.hqch.simple.core.script;

import com.hqch.simple.container.Server;
import com.hqch.simple.resource.Resource;

public interface StartupContext {

	public void registerCache(String name, Resource res);
	
	public void addRemoteServer(Resource res);
	
	public void initServer(Server server);
	
	public void printLog(String msg);
}
