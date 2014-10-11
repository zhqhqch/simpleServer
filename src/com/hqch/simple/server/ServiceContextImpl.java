package com.hqch.simple.server;

import com.hqch.simple.StringUtil;
import com.hqch.simple.container.GameSession;
import com.hqch.simple.netty.io.RequestInfo;

public class ServiceContextImpl implements ServiceContext {

	private RequestInfo request;
	
	public ServiceContextImpl(RequestInfo request){
		this.request = request;
	}

	@Override
	public GameSession getSession() {
		return request.getSession();
	}

	@Override
	public String getAsString(String key) {
		return String.valueOf(request.getData().get(key));
	}

	@Override
	public String getAsString(boolean require, String key) {
		String retStr = String.valueOf(request.getData().get(key));
		if(require && StringUtil.isNull(retStr)){
			throw new IllegalArgumentException("key:"+key+" can not be null.");
		}
		return retStr;
	}

	@Override
	public Integer getAsInt(String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(StringUtil.isNull(temp)){
			return 0;
		}
		return Integer.valueOf(temp);
	}

	@Override
	public Integer getAsInt(boolean require, String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(require && StringUtil.isNull(temp)){
			throw new IllegalArgumentException("key:"+key+" can not be null.");
		} else if(StringUtil.isNull(temp)){
			return 0;
		}
		return Integer.valueOf(temp);
	}

	@Override
	public Long getAsLong(String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(StringUtil.isNull(temp)){
			return 0L;
		}
		return Long.valueOf(temp);
	}

	@Override
	public Long getAsLong(boolean require, String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(require && StringUtil.isNull(temp)){
			throw new IllegalArgumentException("key:"+key+" can not be null.");
		} else if(StringUtil.isNull(temp)){
			return 0L;
		}
		return Long.valueOf(temp);
	}

	@Override
	public Double getAsDouble(String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(StringUtil.isNull(temp)){
			return 0D;
		}
		return Double.valueOf(temp);
	}

	@Override
	public Double getAsDouble(boolean require, String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(require && StringUtil.isNull(temp)){
			throw new IllegalArgumentException("key:"+key+" can not be null.");
		} else if(StringUtil.isNull(temp)){
			return 0D;
		}
		return Double.valueOf(temp);
	}

	@Override
	public Float getAsFloat(String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(StringUtil.isNull(temp)){
			return 0F;
		}
		return Float.valueOf(temp);
	}

	@Override
	public Float getAsFloat(boolean require, String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(require && StringUtil.isNull(temp)){
			throw new IllegalArgumentException("key:"+key+" can not be null.");
		} else if(StringUtil.isNull(temp)){
			return 0F;
		}
		return Float.valueOf(temp);
	}

	@Override
	public Boolean getAsBoolean(String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(StringUtil.isNull(temp)){
			return false;
		}
		return Boolean.valueOf(temp);
	}

	@Override
	public Boolean getAsBoolean(boolean require, String key) {
		String temp = String.valueOf(request.getData().get(key));
		if(require && StringUtil.isNull(temp)){
			throw new IllegalArgumentException("key:"+key+" can not be null.");
		} else if(StringUtil.isNull(temp)){
			return false;
		}
		return Boolean.valueOf(temp);
	}
}
