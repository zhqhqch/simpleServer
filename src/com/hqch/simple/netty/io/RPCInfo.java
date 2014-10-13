package com.hqch.simple.netty.io;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.jboss.netty.channel.Channel;

public class RPCInfo implements Serializable {

	private static final long serialVersionUID = 8904132638292552965L;
	
	private String id;
	
	private Class<?> targetClass;
	private String methodName;
	private Method method;
	private Class<?> returnType;
	private Class<?>[] parameterTypes;
	private Object[] args;
	private Object ret;
	private Throwable exception;
	private long useTime;
	
	private Channel channel;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public Class<?> getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Class<?> getReturnType() {
		return returnType;
	}
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public Object getRet() {
		return ret;
	}
	public void setRet(Object ret) {
		this.ret = ret;
	}
	public Throwable getException() {
		return exception;
	}
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	public long getUseTime() {
		return useTime;
	}
	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}
	@Override
	public String toString() {
		return "RPCInfo [id=" + id + ", targetClass=" + targetClass
				+ ", methodName=" + methodName + ", method=" + method
				+ ", returnType=" + returnType + ", parameterTypes="
				+ Arrays.toString(parameterTypes) + ", args="
				+ Arrays.toString(args) + ", ret=" + ret + ", exception="
				+ exception + ", useTime=" + useTime + "]";
	}
}
