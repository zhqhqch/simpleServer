package com.hqch.simple.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.hqch.simple.resource.Resource;
import com.hqch.simple.rpc.client.RPCClient;

public class RPCManager {

	private static List<RPCClient> clientList;
	
	private static RPCManager manager;
	
	private AtomicInteger invokeCount;
	
	private RPCManager(){
		clientList = new ArrayList<RPCClient>();
		invokeCount = new AtomicInteger();
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
}
