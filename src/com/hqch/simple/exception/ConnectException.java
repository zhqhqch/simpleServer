package com.hqch.simple.exception;

public class ConnectException extends BizException {

	private static final long serialVersionUID = 4051333502726336620L;

	public ConnectException(String msg, Throwable e){
		super(msg, e);
	}
	
	public ConnectException(String msg){
		super(msg);
	}
}
