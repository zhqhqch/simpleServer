package com.hqch.simple.container;

public abstract class Server {

	protected int port;
	protected String protocol;
	protected boolean synchroData;
	protected String cachedName;
	protected int totalRecordTimes;
	protected int minRecordTimes;
	protected int reqeustIntervalSecond;
	
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

	public int getTotalRecordTimes() {
		return totalRecordTimes;
	}

	public void setTotalRecordTimes(int totalRecordTimes) {
		this.totalRecordTimes = totalRecordTimes;
	}

	public int getMinRecordTimes() {
		return minRecordTimes;
	}

	public void setMinRecordTimes(int minRecordTimes) {
		this.minRecordTimes = minRecordTimes;
	}

	public int getReqeustIntervalSecond() {
		return reqeustIntervalSecond;
	}

	public void setReqeustIntervalSecond(int reqeustIntervalSecond) {
		this.reqeustIntervalSecond = reqeustIntervalSecond;
	}
}
