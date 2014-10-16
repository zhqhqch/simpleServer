package com.hqch.simple.core.script;

import com.hqch.simple.container.Server;
import com.hqch.simple.exception.BizException;
import com.hqch.simple.resource.Resource;
import com.hqch.simple.resource.sql.ConnectionResource;

public interface StartupContext {

	public void registerCache(String name, Resource res) throws BizException;
	
	public void addRemoteServer(Resource res);
	
	public void initServer(Server server) throws BizException;
	
	public void setLogLevel(String level);
	
	public void registerResource(String name, ConnectionResource dataSource) throws BizException;
}
