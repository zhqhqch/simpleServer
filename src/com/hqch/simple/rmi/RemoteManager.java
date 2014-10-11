package com.hqch.simple.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hqch.simple.cache.Resource;

public class RemoteManager {

	private static Logger logger = Logger.getLogger(RemoteManager.class);
	
	private static Map<String, Resource> CLIENT_MAP = new HashMap<String, Resource>();
	
	private static final String RMI_BING = "rmi://%s:%s/%s";
	
	public static void addClient(Resource remote){
		
		CLIENT_MAP.put(remote.getName(), remote);
	}
	
	public static Resource getRemoteServer(int remoteId){
		return CLIENT_MAP.get(remoteId);
	}
	
	public static Object createRemoteService(Class<?> clazz, int prefix) throws MalformedURLException, RemoteException, NotBoundException {
		if (clazz == null) {
			throw new NullPointerException(
					"clazz must not be null for RemoteProxy.create()");
		} else {
			Resource remote = CLIENT_MAP.get(prefix);
			String remoteService = String.format(RMI_BING, remote.getHost(),
					remote.getPort(), clazz.getSimpleName());
			Object obj = Naming.lookup(remoteService);
			logger.info("create remote:" + remoteService);
			return obj;
		}
	}
}
