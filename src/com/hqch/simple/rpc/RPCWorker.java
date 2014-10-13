package com.hqch.simple.rpc;

import com.hqch.simple.netty.io.RPCInfo;
import com.hqch.simple.netty.io.RPCRequest;

public class RPCWorker implements Runnable {

	private RPCInfo info;
	
	public RPCWorker(RPCInfo info){
		this.info = info;
	}
	
	@Override
	public void run() {
		info.getChannel().write(getRequest());
	}

	private RPCRequest getRequest(){
		RPCRequest req=new RPCRequest();
		req.setTargetClass(info.getTargetClass().getName());
		req.setMethodName(info.getMethodName());
		Class<?>pp[]= info.getMethod().getParameterTypes();
		String str[]=new String[pp.length];
		for(int i=0;i<str.length;i++){
			str[i]=pp[i].getName();
		}
		req.setParameterTypes(str);
		req.setParameters(info.getArgs());
		return req;
	}
}
