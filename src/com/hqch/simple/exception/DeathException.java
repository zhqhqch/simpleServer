package com.hqch.simple.exception;

public class DeathException extends BizException {

	private static final long serialVersionUID = 2598655315175189156L;
	
	public DeathException(String msg) {
		super(msg);
		System.exit(0);
	}
	
	public DeathException(String msg, Throwable e){
		super(msg, e);
		System.exit(0);
	}

}
