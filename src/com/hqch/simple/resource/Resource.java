package com.hqch.simple.resource;

public class Resource {

	private String name;
	
	private String host;
	
	private int port;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Resource [name=" + name + ", host=" + host + ", port=" + port + "]";
	}
}
