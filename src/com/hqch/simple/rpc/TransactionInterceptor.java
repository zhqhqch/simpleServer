package com.hqch.simple.rpc;

import java.lang.reflect.Method;

import com.hqch.simple.container.InvokeInterceptor;
import com.hqch.simple.exception.BizException;
import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.resource.sql.ConnectionResource;

public class TransactionInterceptor implements InvokeInterceptor {

	
	@Override
	public void afterInvoke(RPCInfo i) throws BizException {
		if(needTransaction(i)){
			ConnectionResource.commit();
		}
	}

	
	@Override
	public void beforeInvoke(RPCInfo i) throws BizException {
		ConnectionResource.startTransaction(needTransaction(i));
	}

	
	@Override
	public void endInvoke(RPCInfo i) throws BizException {
		ConnectionResource.endTransaction();
	}

	
	@Override
	public void onError(RPCInfo i) throws BizException {
		if(needTransaction(i)){
			ConnectionResource.rollback();
		}
	}

	
	private boolean needTransaction(RPCInfo i){
		Method method = i.getMethod();
		return method.isAnnotationPresent(Transaction.class);
	}
}
