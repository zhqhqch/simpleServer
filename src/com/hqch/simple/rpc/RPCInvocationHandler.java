package com.hqch.simple.rpc;

import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.rpc.client.RPCClient;

public class RPCInvocationHandler extends ActionInvocationHandler {
	
	@Override
	public Object invoke(RPCInfo info) throws Throwable {
		RPCClient client = RPCManager.getInstance().getClient();
		return client.invoke(info);
	}

}
