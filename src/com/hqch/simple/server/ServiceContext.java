package com.hqch.simple.server;

import com.hqch.simple.container.GameSession;

public interface ServiceContext {

	public GameSession getSession();
	public void sendMessage(String serviceID, Object message);
	
	public String getAsString(String key);
	public String getAsString(boolean require,String key);

	public Integer getAsInt(String key);
	public Integer getAsInt(boolean require,String key);

	public Long getAsLong(String key);
	public Long getAsLong(boolean require,String key);

	public Double getAsDouble(String key);
	public Double getAsDouble(boolean require,String key);

	public Float getAsFloat(String key);
	public Float getAsFloat(boolean require,String key);

	public Boolean getAsBoolean(String key);
	public Boolean getAsBoolean(boolean require,String key);
}
