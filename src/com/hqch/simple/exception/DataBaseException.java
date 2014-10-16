package com.hqch.simple.exception;

public class DataBaseException extends RuntimeException {

	private static final long serialVersionUID = 6031811406715791068L;

	public DataBaseException(String msg, Throwable e){
		super(msg, e);
	}
	
	public DataBaseException(String msg){
		super(msg);
	}
	
	public DataBaseException(Throwable e){
		super(e);
	}
}
