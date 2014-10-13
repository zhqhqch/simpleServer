package com.hqch.simple.rpc;

import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.rpc.client.RPCClient;

public class RPCInvocationHandler extends ActionInvocationHandler {
	
	@Override
	public Object invoke(RPCInfo info) throws Throwable {
		RPCClient client = RPCManager.getInstance().getClient();
		if(!client.isConnected()){
			client.connect();
		}
		info.setChannel(client.getChannel());
		return client.invoke(info);
	}

}
