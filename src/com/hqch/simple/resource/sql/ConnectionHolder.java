package com.hqch.simple.resource.sql;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class ConnectionHolder implements Connection {

	private Connection realConnection;
	/**
	 * Constructor for ConnectionHolder.
	 * @param c Connection
	 */
	public ConnectionHolder(Connection c) {
		realConnection=c;
	}
	
	/**
	
	 * @return the realConnection */
	public Connection getRealConnection() {
		return realConnection;
	}

	/**
	 * @param realConnection the realConnection to set
	 */
	public void setRealConnection(Connection realConnection) {
		this.realConnection = realConnection;
	}

	/**
	
	
	 * @throws SQLException * @see java.sql.Connection#clearWarnings() */
	public void clearWarnings() throws SQLException {
		realConnection.clearWarnings();
	}
	/**
	 * Release real connection if sb forgot to do this.
	 * @throws Throwable
	 */
	protected void finalize() throws Throwable {
		super.finalize();
		if(realConnection!=null&&!realConnection.isClosed()){
			realConnection.close();
		}
	}
	/**
	
	
	 * @throws SQLException * @see java.sql.Connection#close() */
	public void close() throws SQLException {
		//do NOTHING
		//realConnection.close();
	}
	/**
	
	
	 * @throws SQLException * @see java.sql.Connection#commit() */
	public void commit() throws SQLException {
		//do NOTHING
		//realConnection.commit();
	}
	/**
	
	
	 * @throws SQLException * @see java.sql.Connection#rollback() */
	public void rollback() throws SQLException {
		//do NOTHING
		//realConnection.rollback();
	}
	
	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param savepoint
	 * @throws SQLException * @see java.sql.Connection#rollback(java.sql.Savepoint) */
	public void rollback(Savepoint savepoint) throws SQLException {
		//do NOTHING
		//realConnection.rollback(savepoint);
	}
	
	/**
	 * @return Statement
	 * @throws SQLException * @see java.sql.Connection#createStatement() */
	public Statement createStatement() throws SQLException {
		return realConnection.createStatement();
	}
	/**
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @param resultSetHoldability
	
	
	 * @return Statement
	 * @throws SQLException * @see java.sql.Connection#createStatement(int, int, int) */
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return realConnection.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}
	/**
	 * @param resultSetType
	 * @param resultSetConcurrency
	
	
	 * @return Statement
	 * @throws SQLException * @see java.sql.Connection#createStatement(int, int) */
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return realConnection.createStatement(resultSetType,
				resultSetConcurrency);
	}
	/**
	
	
	 * @return boolean
	 * @throws SQLException * @see java.sql.Connection#getAutoCommit() */
	public boolean getAutoCommit() throws SQLException {
		return realConnection.getAutoCommit();
	}
	/**
	
	
	 * @return String
	 * @throws SQLException * @see java.sql.Connection#getCatalog() */
	public String getCatalog() throws SQLException {
		return realConnection.getCatalog();
	}
	/**
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Connection#getHoldability() */
	public int getHoldability() throws SQLException {
		return realConnection.getHoldability();
	}
	/**
	
	
	 * @return DatabaseMetaData
	 * @throws SQLException * @see java.sql.Connection#getMetaData() */
	public DatabaseMetaData getMetaData() throws SQLException {
		return realConnection.getMetaData();
	}
	/**
	
	
	 * @return int
	 * @throws SQLException * @see java.sql.Connection#getTransactionIsolation() */
	public int getTransactionIsolation() throws SQLException {
		return realConnection.getTransactionIsolation();
	}
	/**
	
	
	 * @return Map<String,Class<?>>
	 * @throws SQLException * @see java.sql.Connection#getTypeMap() */
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return realConnection.getTypeMap();
	}
	/**
	
	
	 * @return SQLWarning
	 * @throws SQLException * @see java.sql.Connection#getWarnings() */
	public SQLWarning getWarnings() throws SQLException {
		return realConnection.getWarnings();
	}
	/**
	
	
	 * @return boolean
	 * @throws SQLException * @see java.sql.Connection#isClosed() */
	public boolean isClosed() throws SQLException {
		return realConnection.isClosed();
	}
	/**
	
	
	 * @return boolean
	 * @throws SQLException * @see java.sql.Connection#isReadOnly() */
	public boolean isReadOnly() throws SQLException {
		return realConnection.isReadOnly();
	}
	/**
	 * @param sql
	
	
	 * @return String
	 * @throws SQLException * @see java.sql.Connection#nativeSQL(java.lang.String) */
	public String nativeSQL(String sql) throws SQLException {
		return realConnection.nativeSQL(sql);
	}
	/**
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @param resultSetHoldability
	
	
	 * @return CallableStatement
	 * @throws SQLException * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int) */
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return realConnection.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}
	/**
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	
	
	 * @return CallableStatement
	 * @throws SQLException * @see java.sql.Connection#prepareCall(java.lang.String, int, int) */
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return realConnection.prepareCall(sql, resultSetType,
				resultSetConcurrency);
	}
	/**
	 * @param sql
	
	
	 * @return CallableStatement
	 * @throws SQLException * @see java.sql.Connection#prepareCall(java.lang.String) */
	public CallableStatement prepareCall(String sql) throws SQLException {
		return realConnection.prepareCall(sql);
	}
	/**
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @param resultSetHoldability
	
	
	 * @return PreparedStatement
	 * @throws SQLException * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int) */
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		PreparedStatement realPs= realConnection.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
		return new PreparedStatementHolder(realPs,sql);
	}
	/**
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	
	
	 * @return PreparedStatement
	 * @throws SQLException * @see java.sql.Connection#prepareStatement(java.lang.String, int, int) */
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		PreparedStatement realPs= 
			realConnection.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
		PreparedStatementHolder psHolder=new PreparedStatementHolder(realPs,sql);
		return psHolder;
	}
	/**
	 * @param sql
	 * @param autoGeneratedKeys
	
	
	 * @return PreparedStatement
	 * @throws SQLException * @see java.sql.Connection#prepareStatement(java.lang.String, int) */
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		PreparedStatement realPs=  
			realConnection.prepareStatement(sql, autoGeneratedKeys);
		PreparedStatementHolder psHolder=new PreparedStatementHolder(realPs,sql);
		return psHolder;
	}
	/**
	 * @param sql
	 * @param columnIndexes
	
	
	 * @return PreparedStatement
	 * @throws SQLException * @see java.sql.Connection#prepareStatement(java.lang.String, int[]) */
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		PreparedStatement realPs=
			realConnection.prepareStatement(sql, columnIndexes);
		PreparedStatementHolder psHolder=new PreparedStatementHolder(realPs,sql);
		return psHolder;
	}
	/**
	 * @param sql
	 * @param columnNames
	
	
	 * @return PreparedStatement
	 * @throws SQLException * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[]) */
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		PreparedStatement realPs=
			realConnection.prepareStatement(sql, columnNames);
		PreparedStatementHolder psHolder=new PreparedStatementHolder(realPs,sql);
		return psHolder;
	}
	/**
	 * @param sql
	
	
	 * @return PreparedStatement
	 * @throws SQLException * @see java.sql.Connection#prepareStatement(java.lang.String) */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement realPs=realConnection.prepareStatement(sql);
		PreparedStatementHolder psHolder=new PreparedStatementHolder(realPs,sql);
		return psHolder;
	}
	/**
	 * @param savepoint
	
	
	 * @throws SQLException * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint) */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		realConnection.releaseSavepoint(savepoint);
	}
	
	
	/**
	 * @param catalog
	
	
	 * @throws SQLException * @see java.sql.Connection#setCatalog(java.lang.String) */
	public void setCatalog(String catalog) throws SQLException {
		realConnection.setCatalog(catalog);
	}
	/**
	 * @param holdability
	
	
	 * @throws SQLException * @see java.sql.Connection#setHoldability(int) */
	public void setHoldability(int holdability) throws SQLException {
		realConnection.setHoldability(holdability);
	}
	/**
	 * @param readOnly
	
	
	 * @throws SQLException * @see java.sql.Connection#setReadOnly(boolean) */
	public void setReadOnly(boolean readOnly) throws SQLException {
		realConnection.setReadOnly(readOnly);
	}
	/**
	
	
	 * @return Savepoint
	 * @throws SQLException * @see java.sql.Connection#setSavepoint() */
	public Savepoint setSavepoint() throws SQLException {
		return realConnection.setSavepoint();
	}
	/**
	 * @param name
	
	
	 * @return Savepoint
	 * @throws SQLException * @see java.sql.Connection#setSavepoint(java.lang.String) */
	public Savepoint setSavepoint(String name) throws SQLException {
		return realConnection.setSavepoint(name);
	}
	/**
	 * @param level
	
	
	 * @throws SQLException * @see java.sql.Connection#setTransactionIsolation(int) */
	public void setTransactionIsolation(int level) throws SQLException {
		realConnection.setTransactionIsolation(level);
	}
	/**
	 * @param arg0
	
	
	 * @throws SQLException * @see java.sql.Connection#setTypeMap(java.util.Map) */
	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		realConnection.setTypeMap(arg0);
	}
	
	//--------------------------------------------------------------------------
	//XXX jdk1.6
	
	/**
	 * @param typeName String
	 * @param elements Object[]
	 * @return Array
	 * @throws SQLException
	 * @see java.sql.Connection#createArrayOf(String, Object[])
	 */
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return ((ConnectionHolder) realConnection).createArrayOf(typeName, elements);
	}
	/**
	 * @return Blob
	 * @throws SQLException
	 * @see java.sql.Connection#createBlob()
	 */
	public Blob createBlob() throws SQLException {
		return ((ConnectionHolder) realConnection).createBlob();
	}
	/**
	 * @return Clob
	 * @throws SQLException
	 * @see java.sql.Connection#createClob()
	 */
	public Clob createClob() throws SQLException {
		return ((ConnectionHolder) realConnection).createClob();
	}
	/**
	 * @return NClob
	 * @throws SQLException
	 * @see java.sql.Connection#createNClob()
	 */
	public NClob createNClob() throws SQLException {
		return realConnection.createNClob();
	}
	/**
	 * @return SQLXML
	 * @throws SQLException
	 * @see java.sql.Connection#createSQLXML()
	 */
	public SQLXML createSQLXML() throws SQLException {
		return ((ConnectionHolder) realConnection).createSQLXML();
	}

	/**
	 * @param typeName String
	 * @param attributes Object[]
	 * @return Struct
	 * @throws SQLException
	 * @see java.sql.Connection#createStruct(String, Object[])
	 */
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return ((ConnectionHolder) realConnection).createStruct(typeName, attributes);
	}
	/**
	 * @return Properties
	 * @throws SQLException
	 * @see java.sql.Connection#getClientInfo()
	 */
	public Properties getClientInfo() throws SQLException {
		return ((ConnectionHolder) realConnection).getClientInfo();
	}

	/**
	 * @param name String
	 * @return String
	 * @throws SQLException
	 * @see java.sql.Connection#getClientInfo(String)
	 */
	public String getClientInfo(String name) throws SQLException {
		return ((ConnectionHolder) realConnection).getClientInfo(name);
	}

	/**
	 * @param timeout int
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Connection#isValid(int)
	 */
	public boolean isValid(int timeout) throws SQLException {
		return ((ConnectionHolder) realConnection).isValid(timeout);
	}
	/**
	 * @param iface Class<?>
	 * @return boolean
	 * @throws SQLException
	 * @see java.sql.Wrapper#isWrapperFor(Class<?>)
	 */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return ((ConnectionHolder) realConnection).isWrapperFor(iface);
	}
	/**
	 * @param properties Properties
	 * @see java.sql.Connection#setClientInfo(Properties)
	 */
	public void setClientInfo(Properties properties) {
		((ConnectionHolder) realConnection).setClientInfo(properties);
	}
	/**
	 * @param name String
	 * @param value String
	 * @see java.sql.Connection#setClientInfo(String, String)
	 */
	public void setClientInfo(String name, String value) {
		((ConnectionHolder) realConnection).setClientInfo(name, value);
	}
	/**
	 * @param iface Class<T>
	 * @return T
	 * @throws SQLException
	 * @see java.sql.Wrapper#unwrap(Class<T>)
	 */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return ((ConnectionHolder) realConnection).unwrap(iface);
	}
}
