/**
 * 
 */
package com.hqch.simple.resource.sql;

import java.sql.Connection;



public class ConnectionResourceDAO extends BaseDAO {
	private ConnectionResource connectionResource;
	/**
	 * Method getConnection.
	 * @return Connection
	 */
	public Connection getConnection() {
		return connectionResource.getConnection();
	}
	/**
	 * Method getConnectionResource.
	 * @return ConnectionResource
	 */
	public ConnectionResource getConnectionResource() {
		return connectionResource;
	}
	/**
	 * Method setConnectionResource.
	 * @param connectionResource ConnectionResource
	 */
	public void setConnectionResource(ConnectionResource connectionResource) {
		this.connectionResource = connectionResource;
	}
	
}
