package com.hqch.simple.resource;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.log4j.Logger;

import com.hqch.simple.log.LoggerFactory;

public class MemcachedResource {

	private static Logger logger = LoggerFactory.getLogger(MemcachedResource.class);
	
	private static MemcachedClient client;
	
	private static final int TIMEOUT = 60;

	public MemcachedResource (Resource res) {
		if (client != null || res == null) return;
		String serverUrl = res.getHost() + ":" + res.getPort();
		try {
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(
					AddrUtil.getAddresses(serverUrl));
			builder.setCommandFactory(new BinaryCommandFactory());//use binary protocol
			client = builder.build(); 
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void save(String key, Object value) {
		save(key, value, TIMEOUT);
	}
	
	public void save(String key, Object value, int timeout) {
		try {
			client.set(key, timeout, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public <T> T get(String key) {
		T t = null;
		try {
			t = client.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return t;
	}
	
	public void remove(String key) {
		try {
			client.delete(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
