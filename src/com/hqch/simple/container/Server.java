package com.hqch.simple.container;

public abstract class Server {

	protected int port;
	protected String protocol;
	protected boolean synchroData;
	protected String cachedName;
	
	public abstract void start() throws Exception;
	
	public abstract void stop() throws Exception;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public boolean isSynchroData() {
		return synchroData;
	}

	public void setSynchroData(boolean synchroData) {
		this.synchroData = synchroData;
	}

	public String getCachedName() {
		return cachedName;
	}

	public void setCachedName(String cachedName) {
		this.cachedName = cachedName;
	}
	
}
