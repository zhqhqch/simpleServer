package com.hqch.simple.container;

import com.hqch.simple.netty.io.RPCInfo;

public interface InvokeInterceptor {
	
	public void beforeInvoke(RPCInfo i);

	public void afterInvoke(RPCInfo i);

	public void endInvoke(RPCInfo i);

	public void onError(RPCInfo i);
}
