package com.hqch.simple.container;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import com.hqch.simple.core.script.StartupContext;
import com.hqch.simple.core.script.StartupContextImpl;
import com.hqch.simple.exception.ScriptException;
import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.netty.io.GameResponseThread;
import com.hqch.simple.resource.MemcachedResource;
import com.hqch.simple.resource.Resource;
import com.hqch.simple.rpc.AbstractProxyFactory;
import com.hqch.simple.rpc.RPCManager;
import com.hqch.simple.rpc.RPCProxyFactory;

public class Container {

	private  Logger logger = LoggerFactory.getLogger(Container.class);
	
	private static final String STARTUP = "/js/startup.js";
	private static final String RMI_BING = "rmi://%s:%s/%s";
	
	private static Map<String, MemcachedResource> resourceMap;
	
	private static Map<String, RPCProxyFactory> proxyFactoryMap;
	
	private static Container container = new Container();
	
	private Map<String, Server> serverMap;
	
	private Map<String, GameSession> allSession;
	
	private Container(){
		resourceMap = new HashMap<String, MemcachedResource>();
		allSession = new ConcurrentHashMap<String, GameSession>();
		serverMap = new HashMap<String, Server>();
		proxyFactoryMap = new HashMap<String, RPCProxyFactory>();
	}
	
	public static Container get(){
		return container;
	}
	
	public void init (){
		try {
			initScript();
		} catch (Exception e) {
			logger.error("startup error for " + STARTUP, e);
		}
	}
	
	private void initScript() throws Exception {
		File file = new File(System.getProperty("user.dir") + File.separator + STARTUP);
		FileInputStream is = new FileInputStream(file);
		
		StartupContext sc = new StartupContextImpl(this);
		ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
		SimpleScriptContext ssc = new SimpleScriptContext();
		Bindings bindings = engine.createBindings(); 
		bindings.put("$", sc);
		ssc.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		try {
			engine.eval(new InputStreamReader(is), ssc);
		} catch (javax.script.ScriptException e) {
			throw new ScriptException(e.getMessage(),e);
		}
	}
	
	public void registerCache(String name, Resource res) {
		MemcachedResource resource = new MemcachedResource(res);
		resourceMap.put(name, resource);
		
		logger.info("cache was init." + res);
	}

	public void registerRemote(Resource res) {
		try {
			String remoteService = String.format(RMI_BING, res.getHost(),
					res.getPort(), res.getName());
			logger.info("create remote:" + remoteService);
			Naming.rebind(remoteService, null);
		} catch (RemoteException e) {
			logger.error("RemoteException", e);
		} catch (MalformedURLException e) {
			logger.error("MalformedURLException", e);
		}
	}

	public void addRemoteServer(Resource res) {
		boolean success = RPCManager.getInstance().addClient(res);
		if(success){
			logger.info("remote server was init." + res);
			
			RPCProxyFactory proxyFactory = proxyFactoryMap.get(res.getName());
			if(proxyFactory==null){
				proxyFactory = new RPCProxyFactory();
				proxyFactoryMap.put(res.getName(),proxyFactory);
			}
		}
	}

	public void initServer(Server server) {
		try {
			server.start();
			serverMap.put(server.getClass().getSimpleName(), server);
		} catch (Exception e) {
			logger.error("server init was error.", e);
			System.exit(0);
		}
	}
	
	public Server getServer(String name){
		return serverMap.get(name);
	}
	
	public GameSession getGameSessionByID(String sessionID){
		return allSession.get(sessionID);
	}
	
	public GameSession createSession(String sessionID, Channel channel, GameResponseThread responseThread){
		GameSession session = new GameSessionImpl(sessionID, channel, responseThread);
		allSession.put(sessionID, session);
		
		return session;
	}
	
	public <T> T createRemoteAction(Class<T>clazz,String clusterName){
		AbstractProxyFactory proxyFactory = proxyFactoryMap.get(clusterName);
		if(proxyFactory == null){
			throw new IllegalArgumentException("can not find cluster:"
					+clusterName);
		}
		return (T) proxyFactory.create(clazz);
	}
}
