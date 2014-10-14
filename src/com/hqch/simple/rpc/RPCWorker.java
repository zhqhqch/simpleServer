package com.hqch.simple.rpc;

import com.hqch.simple.netty.io.RPCInfo;

public class RPCWorker implements Runnable {

	private RPCInfo info;
	
	public RPCWorker(RPCInfo info){
		this.info = info;
	}
	
	@Override
	public void run() {
		info.sendRequest();
	}

}
