package com.hqch.simple.resource.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.hqch.simple.log.LoggerFactory;


public class PreparedStatementHolder implements PreparedStatement{
	private static Logger logger = LoggerFactory.getLogger(PreparedStatementHolder.class);
	private StringBuilder sql;
	private PreparedStatement realStatement;
	private List<Object[]>parameters;
	private Object[]parameter;
	private int paramSize=0;
	private static final int SLOW_SQL_TIME=1000;
	/**
	 * Constructor for PreparedStatementHolder.
	 * @param ps PreparedStatement
	 * @param sqlStr String
	 */
	public PreparedStatementHolder(PreparedStatement ps,String sqlStr) {
		realStatement=ps;
		sql = new StringBuilder(sqlStr);
		parameters=new ArrayList<Object[]>();
		parameter=new Object[128];
		parameters.add(parameter);
	}
	//
	/*
	 * dump SQL parameter
	 */
	private String dumpParameter(Object[]parameter){
		StringBuilder sb=new StringBuilder();
		sb.append("-------------------------------------------------------\n");
		if(paramSize!=0){
			for(int i=0;i<paramSize;i++){
				sb.append("["+i+"]\t:");
				sb.append(parameter[i]+"\n");
			}
		}
		sb.append("-------------------------------------------------------\n");
		return sb.toString();
	}
	//
	private void traceSql(String ret,long startTime) 
	throws SQLException{
		boolean autoCommit=realStatement.getConnection().getAutoCommit();
		long s2=System.currentTimeMillis();
		long time=s2-startTime;
		if(logger.isDebugEnabled()){
			logger.debug("RunSQL:"+dumpSql(autoCommit,time,ret));
		}
		if(time>SLOW_SQL_TIME){
			logger.warn("SlowSQL:"+dumpSql(autoCommit,time,ret));
		}
	}
	//
	private void processError(String ret,long startTime) 
	throws SQLException{
		boolean autoCommit=realStatement.getConnection().getAutoCommit();
		long s2=System.currentTimeMillis();
		logger.error("execute:"+dumpSql(autoCommit,s2-startTime,ret));
	}
	/*
	 * dump sql statement
	 */
	private String dumpStatement(long time){
		return "SQL:"+sql+" [Time]"+time+"\n";
	}
	//
	/*
	 * dump batch
	 */
	private String dumpBatchSql(long time,int[]ret){
		StringBuilder sb=new StringBuilder();
		sb.append("batch:size="+(parameters.size()-1)+"\n");
		sb.append(dumpStatement(time));
		for(int i=0;i<parameters.size()-1;i++){
			sb.append("batch["+(i)+"]");
			sb.append(dumpParameter(parameters.get(i)));
		}
		sb.append("\nRet:");
		if(ret==null){
			sb.append("null");
		}else{
			sb.append(Arrays.toString(ret));
		}
		return sb.toString();
	}
	//
	/**
	 * @return String
	 */
	private String dumpSql(boolean autoCommit,long time,Object ret){
		return "AutoCommit:"+autoCommit+"."
					+dumpStatement(time)+dumpParameter(parameter)+"Ret:"+ret;
	}
	//
	/**
	 * Method setParameter.
	 * @param i int
	 * @param o Object
	 * @throws SQLException
	 */
	private void setParameter(int i,Object o) throws SQLException{
		if(logger.isDebugEnabled()){
			if(i<=0){
				throw new SQLException("parameter index start from 1");
			}
			if(i>paramSize){
				paramSize=i;	
			}
			parameter[i-1]=o;
		}
	}
	/**
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#addBatch() */
	public void addBatch() throws SQLException {
		parameter=new Object[128];
		parameters.add(parameter);
		realStatement.addBatch();
	}
	/**
	 * @param sql
	
	
	 * @throws SQLException * @see java.sql.Statement#addBatch(java.lang.String) */
	public void addBatch(String sql) throws SQLException {
		realStatement.addBatch(sql);
	}
	/**
	
	
	 * @throws SQLException * @see java.sql.Statement#cancel() */
	public void cancel() throws SQLException {
		realStatement.cancel();
	}
	/**
	
	
	 * @throws SQLException * @see java.sql.Statement#clearBatch() */
	public void clearBatch() throws SQLException {
		parameter=new Object[128];
		parameters.clear();
		realStatement.clearBatch();
	}
	/**
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#clearParameters() */
	public void clearParameters() throws SQLException {
		realStatement.clearParameters();
	}
	/**
	
	
	 * @throws SQLException * @see java.sql.Statement#clearWarnings() */
	public void clearWarnings() throws SQLException {
		realStatement.clearWarnings();
	}
	/**
	
	
	 * @throws SQLException * @see java.sql.Statement#close() */
	public void close() throws SQLException {
		realStatement.close();
	}
	/**
	
	
	
	 * @return boolean
	 * @throws SQLException * @see java.sql.PreparedStatement#execute() */
	public boolean execute() throws SQLException {
		long s=System.currentTimeMillis();	
		boolean ret=false;
		try{
			ret=realStatement.execute();
			return ret;
		}catch (SQLException e) {
			processError(ret+"",s);
			throw e;
		}finally{
			traceSql(ret+"",s);
		}
	}
	//
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		long s=System.currentTimeMillis();	
		boolean ret=false;
		try{
			ret= realStatement.execute(sql,autoGeneratedKeys);
			return ret;	
		}catch (SQLException e) {
			processError(ret+"",s);
			throw e;
		}finally{
			traceSql(ret+"",s);
		}
		
	}
	//
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new IllegalStateException("Not supported!");
	}
	//
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		throw new IllegalStateException("Not supported!");
	}
	/**
	 * @param sql
	 * @return boolean
	 * @throws SQLException * @see java.sql.Statement#execute(java.lang.String) */
	public boolean execute(String sql) throws SQLException {
		this.sql.append(sql);
		long s=System.currentTimeMillis();	
		boolean ret=false;
		try{
			ret= realStatement.execute(sql);
			return ret;
		}catch (SQLException e) {
			processError(ret+"",s);
			throw e;
		}finally{
			traceSql(ret+"",s);
		}
	}
	/**
	 * @return int[]
	 * @throws SQLException * @see java.sql.Statement#executeBatch() */
	public int[] executeBatch() throws SQLException {
		long s=System.currentTimeMillis();
		int[]ret=null;
		try{
			ret= realStatement.executeBatch();
			return ret;
		}catch (SQLException e) {
			processError(null,s);
			throw e;
		}finally{
			long s2=System.currentTimeMillis();
			if(logger.isDebugEnabled()){
				logger.debug("executeBatch:"+dumpBatchSql(s2-s,ret));
			}
		}
	}
	/**
	
	
	
	 * @return ResultSet
	 * @throws SQLException * @see java.sql.PreparedStatement#executeQuery() */
	public ResultSet executeQuery() throws SQLException {
		long s=System.currentTimeMillis();
		ResultSet rs=null;
		try{
			rs= realStatement.executeQuery();
			return rs;
		}catch (SQLException e) {
			processError("",s);
			throw e;
		}finally{
			traceSql("",s);
		}
	}
	/**
	 * @param sql
	
	
	
	 * @return ResultSet
	 * @throws SQLException * @see java.sql.Statement#executeQuery(java.lang.String) */
	public ResultSet executeQuery(String sql) throws SQLException {
		this.sql.append(sql);
		long s=System.currentTimeMillis();
		ResultSet rs=null;
		try{
			rs= realStatement.executeQuery(sql);
			return rs;
		}catch (SQLException e) {
			processError("",s);
			throw e;
		}finally{
			traceSql("",s);
		}
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.PreparedStatement#executeUpdate() */
	public int executeUpdate() throws SQLException {
		long s=System.currentTimeMillis();
		int ret=0;
		try{
			ret= realStatement.executeUpdate();
			return ret;
		}catch (SQLException e) {
			processError(ret+"",s);
			throw e;
		}finally{
			traceSql(ret+"",s);
		}
	}
	//
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		throw new IllegalStateException("Not supported!");
	}
	//
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		throw new IllegalStateException("Not supported!");
	}
	//
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		throw new IllegalStateException("Not supported!");
	}
	/**
	 * @param sql
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#executeUpdate(java.lang.String) */
	public int executeUpdate(String sql) throws SQLException {
		this.sql.append(sql);
		long s=System.currentTimeMillis();
		int ret=0;
		try{
			ret= realStatement.executeUpdate(sql);
			return ret;
		}catch (SQLException e) {
			processError(ret+"",s);
			throw e;
		}finally{
			traceSql(ret+"",s);
		}
	}
	/**
	
	
	
	 * @return Connection
	 * @throws SQLException * @see java.sql.Statement#getConnection() */
	public Connection getConnection() throws SQLException {
		return realStatement.getConnection();
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#getFetchDirection() */
	public int getFetchDirection() throws SQLException {
		return realStatement.getFetchDirection();
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#getFetchSize() */
	public int getFetchSize() throws SQLException {
		return realStatement.getFetchSize();
	}
	/**
	
	
	
	 * @return ResultSet
	 * @throws SQLException * @see java.sql.Statement#getGeneratedKeys() */
	public ResultSet getGeneratedKeys() throws SQLException {
		return realStatement.getGeneratedKeys();
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#getMaxFieldSize() */
	public int getMaxFieldSize() throws SQLException {
		return realStatement.getMaxFieldSize();
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#getMaxRows() */
	public int getMaxRows() throws SQLException {
		return realStatement.getMaxRows();
	}
	/**
	
	
	
	 * @return ResultSetMetaData
	 * @throws SQLException * @see java.sql.PreparedStatement#getMetaData() */
	public ResultSetMetaData getMetaData() throws SQLException {
		return realStatement.getMetaData();
	}
	/**
	
	
	
	 * @return boolean
	 * @throws SQLException * @see java.sql.Statement#getMoreResults() */
	public boolean getMoreResults() throws SQLException {
		return realStatement.getMoreResults();
	}
	/**
	 * @param current
	
	
	
	 * @return boolean
	 * @throws SQLException * @see java.sql.Statement#getMoreResults(int) */
	public boolean getMoreResults(int current) throws SQLException {
		return realStatement.getMoreResults(current);
	}
	/**
	
	
	
	 * @return ParameterMetaData
	 * @throws SQLException * @see java.sql.PreparedStatement#getParameterMetaData() */
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return realStatement.getParameterMetaData();
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#getQueryTimeout() */
	public int getQueryTimeout() throws SQLException {
		return realStatement.getQueryTimeout();
	}
	/**
	
	
	
	 * @return ResultSet
	 * @throws SQLException * @see java.sql.Statement#getResultSet() */
	public ResultSet getResultSet() throws SQLException {
		return realStatement.getResultSet();
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#getResultSetConcurrency() */
	public int getResultSetConcurrency() throws SQLException {
		return realStatement.getResultSetConcurrency();
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#getResultSetHoldability() */
	public int getResultSetHoldability() throws SQLException {
		return realStatement.getResultSetHoldability();
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#getResultSetType() */
	public int getResultSetType() throws SQLException {
		return realStatement.getResultSetType();
	}
	/**
	
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Statement#getUpdateCount() */
	public int getUpdateCount() throws SQLException {
		return realStatement.getUpdateCount();
	}
	/**
	
	
	
	 * @return SQLWarning
	 * @throws SQLException * @see java.sql.Statement#getWarnings() */
	public SQLWarning getWarnings() throws SQLException {
		return realStatement.getWarnings();
	}
	/**
	
	
	
	 * @return boolean
	 * @throws SQLException * @see java.sql.Statement#isClosed() */
	public boolean isClosed() throws SQLException {
		return realStatement.isClosed();
	}
	/**
	
	
	
	 * @return boolean
	 * @throws SQLException * @see java.sql.Statement#isPoolable() */
	public boolean isPoolable() throws SQLException {
		return realStatement.isPoolable();
	}
	/**
	 * @param iface
	
	
	
	 * @return boolean
	 * @throws SQLException * @see java.sql.Wrapper#isWrapperFor(java.lang.Class) */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return realStatement.isWrapperFor(iface);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setArray(int, java.sql.Array) */
	public void setArray(int parameterIndex, Array x) throws SQLException {
		realStatement.setArray(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int) */
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		realStatement.setAsciiStream(parameterIndex, x, length);
	}
	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long) */
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		realStatement.setAsciiStream(parameterIndex, x, length);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream) */
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		realStatement.setAsciiStream(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal) */
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setBigDecimal(parameterIndex, x);
	}
	
	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int) */
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		realStatement.setBinaryStream(parameterIndex, x, length);
	}
	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long) */
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		realStatement.setBinaryStream(parameterIndex, x, length);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream) */
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		realStatement.setBinaryStream(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob) */
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		realStatement.setBlob(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param inputStream
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long) */
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		realStatement.setBlob(parameterIndex, inputStream, length);
	}
	/**
	 * @param parameterIndex
	 * @param inputStream
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream) */
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		realStatement.setBlob(parameterIndex, inputStream);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setBoolean(int, boolean) */
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setBoolean(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setByte(int, byte) */
	public void setByte(int parameterIndex, byte x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setByte(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setBytes(int, byte[]) */
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		realStatement.setBytes(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int) */
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		realStatement.setCharacterStream(parameterIndex, reader, length);
	}
	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long) */
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		realStatement.setCharacterStream(parameterIndex, reader, length);
	}
	/**
	 * @param parameterIndex
	 * @param reader
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader) */
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		realStatement.setCharacterStream(parameterIndex, reader);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob) */
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		realStatement.setClob(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long) */
	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		realStatement.setClob(parameterIndex, reader, length);
	}
	/**
	 * @param parameterIndex
	 * @param reader
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setClob(int, java.io.Reader) */
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		realStatement.setClob(parameterIndex, reader);
	}
	/**
	 * @param name
	
	
	 * @throws SQLException * @see java.sql.Statement#setCursorName(java.lang.String) */
	public void setCursorName(String name) throws SQLException {
		realStatement.setCursorName(name);
	}
	/**
	 * @param parameterIndex
	 * @param x
	 * @param cal
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar) */
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setDate(parameterIndex, x, cal);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setDate(int, java.sql.Date) */
	public void setDate(int parameterIndex, Date x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setDate(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setDouble(int, double) */
	public void setDouble(int parameterIndex, double x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setDouble(parameterIndex, x);
	}
	/**
	 * @param enable
	
	
	 * @throws SQLException * @see java.sql.Statement#setEscapeProcessing(boolean) */
	public void setEscapeProcessing(boolean enable) throws SQLException {
		realStatement.setEscapeProcessing(enable);
	}
	/**
	 * @param direction
	
	
	 * @throws SQLException * @see java.sql.Statement#setFetchDirection(int) */
	public void setFetchDirection(int direction) throws SQLException {
		realStatement.setFetchDirection(direction);
	}
	/**
	 * @param rows
	
	
	 * @throws SQLException * @see java.sql.Statement#setFetchSize(int) */
	public void setFetchSize(int rows) throws SQLException {
		realStatement.setFetchSize(rows);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setFloat(int, float) */
	public void setFloat(int parameterIndex, float x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setFloat(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setInt(int, int) */
	public void setInt(int parameterIndex, int x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setInt(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setLong(int, long) */
	public void setLong(int parameterIndex, long x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setLong(parameterIndex, x);
	}
	/**
	 * @param max
	
	
	 * @throws SQLException * @see java.sql.Statement#setMaxFieldSize(int) */
	public void setMaxFieldSize(int max) throws SQLException {
		realStatement.setMaxFieldSize(max);
	}
	/**
	 * @param max
	
	
	 * @throws SQLException * @see java.sql.Statement#setMaxRows(int) */
	public void setMaxRows(int max) throws SQLException {
		realStatement.setMaxRows(max);
	}
	/**
	 * @param parameterIndex
	 * @param value
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long) */
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		realStatement.setNCharacterStream(parameterIndex, value, length);
	}
	/**
	 * @param parameterIndex
	 * @param value
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader) */
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		realStatement.setNCharacterStream(parameterIndex, value);
	}
	/**
	 * @param parameterIndex
	 * @param value
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob) */
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		realStatement.setNClob(parameterIndex, value);
	}
	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long) */
	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		realStatement.setNClob(parameterIndex, reader, length);
	}
	/**
	 * @param parameterIndex
	 * @param reader
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader) */
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		realStatement.setNClob(parameterIndex, reader);
	}
	/**
	 * @param parameterIndex
	 * @param value
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setNString(int, java.lang.String) */
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		realStatement.setNString(parameterIndex, value);
	}
	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @param typeName
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String) */
	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		realStatement.setNull(parameterIndex, sqlType, typeName);
	}
	/**
	 * @param parameterIndex
	 * @param sqlType
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setNull(int, int) */
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, null);
		}
		realStatement.setNull(parameterIndex, sqlType);
	}
	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	 * @param scaleOrLength
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int) */
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement
				.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}
	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int) */
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setObject(parameterIndex, x, targetSqlType);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setObject(int, java.lang.Object) */
	public void setObject(int parameterIndex, Object x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setObject(parameterIndex, x);
	}
	/**
	 * @param poolable
	
	
	 * @throws SQLException * @see java.sql.Statement#setPoolable(boolean) */
	public void setPoolable(boolean poolable) throws SQLException {
		realStatement.setPoolable(poolable);
	}
	/**
	 * @param seconds
	
	
	 * @throws SQLException * @see java.sql.Statement#setQueryTimeout(int) */
	public void setQueryTimeout(int seconds) throws SQLException {
		realStatement.setQueryTimeout(seconds);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref) */
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		realStatement.setRef(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId) */
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		realStatement.setRowId(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setShort(int, short) */
	public void setShort(int parameterIndex, short x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setShort(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param xmlObject
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML) */
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, xmlObject);
		}
		realStatement.setSQLXML(parameterIndex, xmlObject);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setString(int, java.lang.String) */
	public void setString(int parameterIndex, String x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setString(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	 * @param cal
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar) */
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setTime(parameterIndex, x, cal);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setTime(int, java.sql.Time) */
	public void setTime(int parameterIndex, Time x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setTime(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	 * @param cal
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar) */
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setTimestamp(parameterIndex, x, cal);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp) */
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setTimestamp(parameterIndex, x);
	}
	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	
	
	
	 * @deprecated since <unknown>
	 * @throws SQLException * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int) */
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		realStatement.setUnicodeStream(parameterIndex, x, length);
	}
	/**
	 * @param parameterIndex
	 * @param x
	
	
	 * @throws SQLException * @see java.sql.PreparedStatement#setURL(int, java.net.URL) */
	public void setURL(int parameterIndex, URL x) throws SQLException {
		if(logger.isDebugEnabled()){
			setParameter(parameterIndex, x);
		}
		realStatement.setURL(parameterIndex, x);
	}
	/**
	
	 * @param iface
	
	
	
	 * @return T
	 * @throws SQLException * @see java.sql.Wrapper#unwrap(java.lang.Class) */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return realStatement.unwrap(iface);
	}
	
}
