package com.hqch.simple.resource.sql;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.hqch.simple.exception.DataBaseException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0ConnectionResource extends ConnectionResource {

	private ComboPooledDataSource dataSource;

	public C3P0ConnectionResource() {
		dataSource = new ComboPooledDataSource();
		dataSource.setMaxConnectionAge(3600);
	}

	/**
	 * 设置是否在连接关闭后提交
	 * 
	 * @param autoCommitOnClose
	 *            boolean
	 * @throws NamingException
	 */
	public void setAutoCommitOnClose(boolean autoCommitOnClose)
			throws NamingException {
		dataSource.setAutoCommitOnClose(autoCommitOnClose);
	}

	/**
	 * 设置超时时间
	 * 
	 * @param checkoutTimeout
	 *            int
	 * @throws NamingException
	 */
	public void setCheckoutTimeout(int checkoutTimeout) throws NamingException {
		dataSource.setCheckoutTimeout(checkoutTimeout);
	}

	/**
	 * 设置数据源名称
	 * 
	 * @param name
	 *            String
	 * @throws NamingException
	 */
	public void setDataSourceName(String name) throws NamingException {
		dataSource.setDataSourceName(name);
	}

	/**
	 * 设置数据源描述
	 * 
	 * @param description
	 *            String
	 * @throws NamingException
	 */
	public void setDescription(String description) throws NamingException {
		dataSource.setDescription(description);
	}

	/**
	 * 设置驱动名称
	 * 
	 * @param driverClass
	 *            String
	 * @throws PropertyVetoException
	 * @throws NamingException
	 */
	public void setDriverClass(String driverClass)
			throws PropertyVetoException, NamingException {
		dataSource.setDriverClass(driverClass);
	}

	/**
	 * @param idleConnectionTestPeriod
	 *            int
	 * @throws NamingException
	 */
	public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod)
			throws NamingException {
		dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
	}

	/**
	 * 设置初始的连接池大小
	 * 
	 * @param initialPoolSize
	 *            int
	 * @throws NamingException
	 */
	public void setInitialPoolSize(int initialPoolSize) throws NamingException {
		dataSource.setInitialPoolSize(initialPoolSize);
	}

	/**
	 * 设置JDBC url
	 * 
	 * @param jdbcUrl
	 *            String
	 * @throws NamingException
	 */
	public void setJdbcUrl(String jdbcUrl) throws NamingException {
		dataSource.setJdbcUrl(jdbcUrl);
	}

	/**
	 * 
	 * @param maxAdministrativeTaskTime
	 *            int
	 * @throws NamingException
	 */
	public void setMaxAdministrativeTaskTime(int maxAdministrativeTaskTime)
			throws NamingException {
		dataSource.setMaxAdministrativeTaskTime(maxAdministrativeTaskTime);
	}

	/**
	 * @param maxConnectionAge
	 *            int
	 * @throws NamingException
	 */
	public void setMaxConnectionAge(int maxConnectionAge)
			throws NamingException {
		dataSource.setMaxConnectionAge(maxConnectionAge);
	}

	/**
	 * 设置最大空闲时间
	 * 
	 * @param maxIdleTime
	 *            int
	 * @throws NamingException
	 */
	public void setMaxIdleTime(int maxIdleTime) throws NamingException {
		dataSource.setMaxIdleTime(maxIdleTime);
	}

	/**
	 * @param maxIdleTimeExcessConnections
	 *            int
	 * @throws NamingException
	 */
	public void setMaxIdleTimeExcessConnections(int maxIdleTimeExcessConnections)
			throws NamingException {
		dataSource
				.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
	}

	/**
	 * 设置连接池最大大小
	 * 
	 * @param maxPoolSize
	 *            int
	 * @throws NamingException
	 */
	public void setMaxPoolSize(int maxPoolSize) throws NamingException {
		dataSource.setMaxPoolSize(maxPoolSize);
	}

	/**
	 * 
	 * @param maxStatements
	 *            int
	 * @throws NamingException
	 */
	public void setMaxStatements(int maxStatements) throws NamingException {
		dataSource.setMaxStatements(maxStatements);
	}

	/**
	 * @param maxStatementsPerConnection
	 *            int
	 * @throws NamingException
	 */
	public void setMaxStatementsPerConnection(int maxStatementsPerConnection)
			throws NamingException {
		dataSource.setMaxStatementsPerConnection(maxStatementsPerConnection);
	}

	/**
	 * 设置连接池最小大小
	 * 
	 * @param minPoolSize
	 *            int
	 * @throws NamingException
	 */
	public void setMinPoolSize(int minPoolSize) throws NamingException {
		dataSource.setMinPoolSize(minPoolSize);
	}

	/**
	 * @param numHelperThreads
	 *            int
	 * @throws NamingException
	 */
	public void setNumHelperThreads(int numHelperThreads)
			throws NamingException {
		dataSource.setNumHelperThreads(numHelperThreads);
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 *            String
	 * @throws NamingException
	 */
	public void setPassword(String password) throws NamingException {
		dataSource.setPassword(password);
	}

	/**
	 * @param testConnectionOnCheckin
	 *            boolean
	 * @throws NamingException
	 */
	public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin)
			throws NamingException {
		dataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
	}

	/**
	 * @param testConnectionOnCheckout
	 *            boolean
	 * @throws NamingException
	 */
	public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout)
			throws NamingException {
		dataSource.setTestConnectionOnCheckout(testConnectionOnCheckout);
	}

	/**
	 * @param unreturnedConnectionTimeout
	 *            int
	 * @throws NamingException
	 */
	public void setUnreturnedConnectionTimeout(int unreturnedConnectionTimeout)
			throws NamingException {
		dataSource.setUnreturnedConnectionTimeout(unreturnedConnectionTimeout);
	}

	/**
	 * 设置用户
	 * 
	 * @param user
	 *            String
	 * @throws NamingException
	 */
	public void setUser(String user) throws NamingException {
		dataSource.setUser(user);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource#getNumBusyConnections()
	 */
	public int getNumBusyConnections() throws SQLException {
		return dataSource.getNumBusyConnections();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource#getNumConnections()
	 */
	public int getNumConnections() throws SQLException {
		return dataSource.getNumConnections();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource#getNumIdleConnections()
	 */
	public int getNumIdleConnections() throws SQLException {
		return dataSource.getNumIdleConnections();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource#getNumUnclosedOrphanedConnections()
	 */
	public int getNumUnclosedOrphanedConnections() throws SQLException {
		return dataSource.getNumUnclosedOrphanedConnections();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource#getNumUserPools()
	 */
	public int getNumUserPools() throws SQLException {
		return dataSource.getNumUserPools();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource#getThreadPoolNumActiveThreads()
	 */
	public int getThreadPoolNumActiveThreads() throws SQLException {
		return dataSource.getThreadPoolNumActiveThreads();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource#getThreadPoolNumIdleThreads()
	 */
	public int getThreadPoolNumIdleThreads() throws SQLException {
		return dataSource.getThreadPoolNumIdleThreads();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource#getThreadPoolNumTasksPending()
	 */
	public int getThreadPoolNumTasksPending() throws SQLException {
		return dataSource.getThreadPoolNumTasksPending();
	}

	/**
	 * @return Connection
	 */
	@Override
	protected Connection getRealConnection(boolean needTranscation) {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new DataBaseException(e);
		}
	}

	@Override
	public String toString() {
		return "C3P0ConnectionResource [dataSource=" + dataSource + "]";
	}

}
