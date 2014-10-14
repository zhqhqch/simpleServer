package com.hqch.simple.util;

import com.hqch.simple.exception.BizException;

public class ClassUtil {

	public static Class<?>[] getParameterTypes(String[] parameterTypes) throws ClassNotFoundException{
		Class<?>[] retClazzs = new Class<?>[parameterTypes.length];
		int i = 0;
		for(String classType : parameterTypes){
			retClazzs[i++] = Class.forName(classType);
		}
		
		return retClazzs;
	}
	
	public static Object getActionObject(String targetClassName) throws Exception {
		targetClassName = targetClassName.concat("Impl");
		String className = targetClassName.replace("action", "action.impl");
		try {
			Class<?> clazzImpls = Class.forName(className);
			Object obj = clazzImpls.newInstance();
			if(obj == null){
					throw new BizException(
							"can not find instance for class:" + className);
			}
			return obj;
		} catch (Exception e) {
			throw new BizException(
					"can not find instance for class:" + className);
		}
	}
}
