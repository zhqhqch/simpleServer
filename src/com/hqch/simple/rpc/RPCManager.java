package com.hqch.simple.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.resource.Resource;
import com.hqch.simple.rpc.client.RPCClient;

public class RPCManager {

	private static List<RPCClient> clientList;
	
	private static Map<String, RPCInfo> rpcMap;
	
	private static RPCManager manager;
	
	private AtomicInteger invokeCount;
	
	private RPCManager(){
		clientList = new ArrayList<RPCClient>();
		invokeCount = new AtomicInteger();
		rpcMap = new ConcurrentHashMap<String, RPCInfo>();
	}
	
	public static RPCManager getInstance(){
		if(manager == null){
			manager = new RPCManager();
		}
		return manager;
	}
	
	public boolean addClient(Resource res) {
		RPCClient client = new RPCClient(res);
		clientList.add(client);
		return client.isConnected();
	}

	public RPCClient getClient() {
		int size = clientList.size();
		if(size == 0){
			return null;
		}
		
		int cur = invokeCount.getAndIncrement();
		int next = cur % size;
		RPCClient client = null;
		for(int i = next;i<size;i++){
			client = clientList.get(i);
			if(client.isConnected()){
				return client;
			}
		}
		for(int i = 0;i<next;i++){
			client = clientList.get(i);
			if(client.isConnected()){
				return client;
			}
		}
		
		return null;
	}
	
	public void addRPC(RPCInfo info){
		rpcMap.put(info.getId(), info);
	}
	
	public void removeRPC(String id){
		rpcMap.remove(id);
	}
	
	public RPCInfo getRPC(String id){
		return rpcMap.get(id);
	}

	public RPCClient getClientByName(String name) {
		for(RPCClient client : clientList){
			if(name.equals(client.getName())){
				return client;
			}
		}
		return null;
	}
	
}
