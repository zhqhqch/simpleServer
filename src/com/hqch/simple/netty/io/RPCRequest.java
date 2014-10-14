package com.hqch.simple.netty.io;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class RPCRequest implements Serializable {

	private static final long serialVersionUID = 9082848780680086400L;
	private String id;
	private String targetClass;
	private String methodName;
	private String[] parameterTypes;
	private Object[] parameters;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(String[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	public Object[] getParameters() {
		return parameters;
	}
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	@Override
	public String toString() {
		return JSONObject.fromObject(this).toString() + "\n";
	}
}
