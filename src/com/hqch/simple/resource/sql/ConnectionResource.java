package com.hqch.simple.resource.sql;

import java.sql.Connection;
import java.sql.SQLException;

import com.hqch.simple.exception.BizException;
import com.hqch.simple.exception.ConnectException;
import com.hqch.simple.exception.DataBaseException;

public abstract class ConnectionResource {

	private static ThreadLocal<Transcation> transcationHolder = new ThreadLocal<Transcation>();
	
	protected abstract Connection getRealConnection(boolean needTranscation) throws DataBaseException;
	
	public Connection getConnection() {
		Transcation t = transcationHolder.get();
		if(t == null){
			throw new DataBaseException("can not find transcation");
		}
		ConnectionHolder c = t.getConnection();
		if(c == null){
			try{
				Connection conn = getRealConnection(t.isNeedTranscation());
				conn.setAutoCommit(!t.isNeedTranscation());
				
				ConnectionHolder holder = new ConnectionHolder(conn);
				t.setConnection(holder);
				
				return holder;
			} catch (SQLException e) {
				throw new DataBaseException("connection", e);
			}
		}
		
		return c;
	}
	
	public static void commit() throws BizException {
		Transcation t = transcationHolder.get();
		if(t == null){
			throw new ConnectException("can not find transcation");
		}
		ConnectionHolder c = t.getConnection();
		if(c != null){
			try {
				c.getRealConnection().commit();
			} catch (SQLException e) {
				throw new ConnectException("commit", e);
			}
		}
	}

	public static void startTransaction(boolean needTransaction) {
		Transcation t = new Transcation();
		t.setNeedTranscation(needTransaction);
		transcationHolder.set(t);
	}

	public static void endTransaction() throws BizException {
		Transcation t = transcationHolder.get();
		if(t == null){
			throw new ConnectException("can not find transcation");
		}
		ConnectionHolder c = t.getConnection();
		if(c != null){
			try {
				c.getRealConnection().setAutoCommit(true);
				c.getRealConnection().close();
			} catch (SQLException e) {
				throw new ConnectException("endTransaction", e);
			}
		}
	}

	public static void rollback() throws BizException {
		Transcation t = transcationHolder.get();
		if(t == null){
			throw new ConnectException("can not find transcation");
		}
		ConnectionHolder c = t.getConnection();
		if(c != null){
			try {
				c.getRealConnection().rollback();
			} catch (SQLException e) {
				throw new ConnectException("rollback", e);
			}
		}
	}

}
