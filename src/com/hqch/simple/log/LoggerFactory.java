package com.hqch.simple.log;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerFactory {
	private static LoggerFactory factory;

	private LoggerFactory() {
	}

	public static LoggerFactory getInstance() {
		if (factory == null) {
			factory = new LoggerFactory();
			factory.setupLogger();
		}
		return factory;
	}
	
	public static Logger getLogger(Class<?> clazz) {
		return Logger.getLogger(clazz);
	}
	
	public static Logger getLogger(){
		return Logger.getRootLogger();
	}
	
	private void setupLogger() {
		try {
			LogManager.resetConfiguration();// 还原到最初的配置
			Properties pro = new Properties();
			LoggerFactory.class.getClassLoader();
			//
			InputStream input = ClassLoader
					.getSystemClassLoader().getResourceAsStream("log4j.properties");
			pro.load(input);
			PropertyConfigurator.configure(pro);
			//
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setLogLevel(String value) {
		Level level = Level.INFO;
		if (value.equals("OFF")) {
			level = Level.OFF;
		} else if (value.equals("FATAL")) {
			level = Level.FATAL;
		} else if (value.equals("ERROR")) {
			level = Level.ERROR;
		} else if (value.equals("WARN")) {
			level = Level.WARN;
		} else if (value.equals("DEBUG")) {
			level = Level.DEBUG;
		} else if (value.equals("TRACE")) {
			level = Level.TRACE;
		} else if (value.equals("ALL")) {
			level = Level.ALL;
		} else {
			level = Level.INFO;
		}
		
		setLogLevel(level);
	}
	
	public void setLogLevel(Level level) {
		Logger.getRootLogger().setLevel(level);
	}
}
