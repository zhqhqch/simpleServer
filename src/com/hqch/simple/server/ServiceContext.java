package com.hqch.simple.server;

import com.hqch.simple.container.GameSession;

public interface ServiceContext {

	public GameSession getSession();
	
	String getAsString(String key);
	String getAsString(boolean require,String key);

	Integer getAsInt(String key);
	Integer getAsInt(boolean require,String key);

	Long getAsLong(String key);
	Long getAsLong(boolean require,String key);

	Double getAsDouble(String key);
	Double getAsDouble(boolean require,String key);

	Float getAsFloat(String key);
	Float getAsFloat(boolean require,String key);

	Boolean getAsBoolean(String key);
	Boolean getAsBoolean(boolean require,String key);
}
