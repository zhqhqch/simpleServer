package com.hqch.simple.rmi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

public class SMRMISocket extends RMISocketFactory {

	private int port;
	
	public SMRMISocket(int port){
		this.port = port;
	}
	
	@Override
	public Socket createSocket(String host, int port) throws IOException {
		return new Socket(host, port);
	}

	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		if(port == 0){
			port = this.port + 1;
		}
		
		return new ServerSocket(port);
	}

}
