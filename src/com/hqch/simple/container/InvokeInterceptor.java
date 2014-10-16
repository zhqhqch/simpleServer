package com.hqch.simple.container;

import com.hqch.simple.exception.BizException;
import com.hqch.simple.netty.io.RPCInfo;

public interface InvokeInterceptor {
	
	public void beforeInvoke(RPCInfo i) throws BizException;

	public void afterInvoke(RPCInfo i) throws BizException;

	public void endInvoke(RPCInfo i) throws BizException;

	public void onError(RPCInfo i) throws BizException;
}
