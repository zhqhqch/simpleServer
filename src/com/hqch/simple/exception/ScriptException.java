package com.hqch.simple.exception;

public class ScriptException extends Exception {

	private static final long serialVersionUID = -6067332569092501149L;

	public ScriptException(String msg, Throwable e){
		super(msg, e);
	}
	
	public ScriptException(String msg){
		super(msg);
	}
}
