package com.hqch.simple.resource.sql;

import org.apache.log4j.Logger;

import com.hqch.simple.log.LoggerFactory;

public class ConnectionResource {

	private static Logger logger = LoggerFactory.getLogger(ConnectionResource.class);
	private static ThreadLocal<Transcation> transcationHolder = new ThreadLocal<Transcation>();
	
	public static void commit() {
		// TODO Auto-generated method stub
		
	}

	public static void startTransaction(boolean needTransaction) {
		// TODO Auto-generated method stub
		
	}

	public static void endTransaction() {
		// TODO Auto-generated method stub
		
	}

	public static void rollback() {
		// TODO Auto-generated method stub
		
	}

}
