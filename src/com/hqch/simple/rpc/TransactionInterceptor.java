package com.hqch.simple.rpc;

import java.lang.reflect.Method;

import com.hqch.simple.container.InvokeInterceptor;
import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.resource.sql.ConnectionResource;

public class TransactionInterceptor implements InvokeInterceptor {

	
	@Override
	public void afterInvoke(RPCInfo i) {
		if(needTransaction(i)){
			ConnectionResource.commit();
		}
	}

	
	@Override
	public void beforeInvoke(RPCInfo i) {
		ConnectionResource.startTransaction(needTransaction(i));
	}

	
	@Override
	public void endInvoke(RPCInfo i) {
		ConnectionResource.endTransaction();
	}

	
	@Override
	public void onError(RPCInfo i) {
		if(needTransaction(i)){
			ConnectionResource.rollback();
		}
	}

	
	private boolean needTransaction(RPCInfo i){
		Method method = i.getMethod();
		return method.isAnnotationPresent(Transaction.class);
	}
}
