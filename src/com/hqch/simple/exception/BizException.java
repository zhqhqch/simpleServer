package com.hqch.simple.exception;

public class BizException extends Exception {

	private static final long serialVersionUID = 7827182252157865659L;

	public BizException(String msg, Throwable e){
		super(msg, e);
	}
	
	public BizException(String msg){
		super(msg);
	}
}
