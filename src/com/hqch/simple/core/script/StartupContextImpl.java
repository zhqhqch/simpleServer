package com.hqch.simple.core.script;

import com.hqch.simple.container.Container;
import com.hqch.simple.container.Server;
import com.hqch.simple.exception.BizException;
import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.resource.Resource;
import com.hqch.simple.resource.sql.ConnectionResource;

public class StartupContextImpl implements StartupContext {
	
	private Container container;
	
	public StartupContextImpl(Container c){
		this.container = c;
	}
	
	@Override
	public void registerCache(String name, Resource res) throws BizException {
		res.setName(name);
		container.registerCache(name, res);
	}
	
	@Override
	public void addRemoteServer(Resource res) {
		container.addRemoteServer(res);
	}
	
	@Override
	public void setLogLevel(String level){
		LoggerFactory.getInstance().setLogLevel(level);
	}
	
	@Override
	public void initServer(Server server) throws BizException {
		container.initServer(server);
	}

	@Override
	public void registerResource(String name, ConnectionResource dataSource)
			throws BizException {
		container.registerResource(name, dataSource);
	}
	
}
