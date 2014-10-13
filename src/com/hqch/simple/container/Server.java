package com.hqch.simple.container;

public abstract class Server {

	protected int port;
	
	public abstract void start() throws Exception;
	
	public abstract void stop() throws Exception;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
