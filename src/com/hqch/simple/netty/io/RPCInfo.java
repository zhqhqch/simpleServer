package com.hqch.simple.netty.io;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.jboss.netty.channel.Channel;

import com.hqch.simple.util.JavaSerializeUtils;

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
	private RPCRequest rpcRequest;
	private RPCResult result;
	
	private long startTime;
	
	private transient CountDownLatch latch;
	
	public RPCInfo (){
		latch = new CountDownLatch(1);
	}
	
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

	public RPCRequest getRpcRequest() {
		return rpcRequest;
	}
	public void setRpcRequest(RPCRequest rpcRequest) {
		this.rpcRequest = rpcRequest;
	}
	
	public RPCResult getResult() {
		return result;
	}
	public void setResult(RPCResult result) {
		this.result = result;
	}
	
	public CountDownLatch getLatch() {
		return latch;
	}
	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}
	public long getStartTime() {
		return startTime;
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
	
	
	public void sendRequest() {
		channel.write(JavaSerializeUtils.getInstance().getChannelBuffer(getRequest()));
		this.startTime = System.currentTimeMillis();
	}
	
	public void sendResult(){
		channel.write(JavaSerializeUtils.getInstance().getChannelBuffer(result));
	}
	
	private RPCRequest getRequest(){
		RPCRequest req = new RPCRequest();
		req.setId(id);
		req.setTargetClass(targetClass.getName());
		req.setMethodName(methodName);
		Class<?>pp[] = method.getParameterTypes();
		String str[] = new String[pp.length];
		for(int i=0;i<str.length;i++){
			str[i] = pp[i].getName();
		}
		req.setParameterTypes(str);
		req.setParameters(args);
		return req;
	}
}
