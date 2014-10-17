package com.hqch.simple.netty.io;

import java.io.Serializable;

public abstract class BaseVO implements Serializable {

	private static final long serialVersionUID = -9176164414840815072L;

	public abstract String toJSONString();
	
	public abstract byte[] toProtobuf();
}
