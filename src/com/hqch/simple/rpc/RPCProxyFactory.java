package com.hqch.simple.rpc;

public class RPCProxyFactory extends AbstractProxyFactory {

	private RPCInvocationHandler invocationHandler;
	
	public RPCProxyFactory(){
		this.invocationHandler = new RPCInvocationHandler();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T create(Class<T> actionInterface) {
		if(!hasProxyObject(actionInterface)){
			Object proxy = createProxy(actionInterface,invocationHandler);
			addProxyObject(actionInterface, proxy);
		}
		return (T)getProxyObject(actionInterface);
	}
}
