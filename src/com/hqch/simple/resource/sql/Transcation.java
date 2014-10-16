package com.hqch.simple.resource.sql;

public class Transcation {

	private boolean needTranscation;
	private ConnectionHolder connection;
	public boolean isNeedTranscation() {
		return needTranscation;
	}
	public void setNeedTranscation(boolean needTranscation) {
		this.needTranscation = needTranscation;
	}
	public ConnectionHolder getConnection() {
		return connection;
	}
	public void setConnection(ConnectionHolder connection) {
		this.connection = connection;
	}
}
