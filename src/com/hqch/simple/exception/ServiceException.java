package com.hqch.simple.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 7827182252157865659L;

	public ServiceException(String msg, Throwable e){
		super(msg, e);
	}
	
	public ServiceException(String msg){
		super(msg);
	}
}
