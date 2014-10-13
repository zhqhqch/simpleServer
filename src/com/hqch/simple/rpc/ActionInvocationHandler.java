package com.hqch.simple.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import com.hqch.simple.StringUtil;
import com.hqch.simple.netty.io.RPCInfo;

public abstract class ActionInvocationHandler implements InvocationHandler {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		long startTime = System.currentTimeMillis();
		String methodName = method.getName();
		Class<?> parameterTypes[] = method.getParameterTypes();
		Class<?> targetClass = method.getDeclaringClass();
		
		RPCInfo info = new RPCInfo();
		info.setId(StringUtil.generateID());
		info.setArgs(args);
		info.setReturnType(method.getReturnType());
		info.setTargetClass(targetClass);
		info.setMethodName(methodName);
		info.setMethod(method);
		info.setParameterTypes(parameterTypes);
		
		Object ret = null;
		try{
			ret = invoke(info);
			info.setRet(ret);
			return ret;
		}catch (UndeclaredThrowableException e) {
			throw e.getUndeclaredThrowable();
		}catch (Throwable e) {
			throw e;
		}finally{
			long endTime=System.currentTimeMillis();
			info.setUseTime(endTime - startTime);
		}
	}

	public abstract Object invoke(RPCInfo info) throws Throwable;
}
