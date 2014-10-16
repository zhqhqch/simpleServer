package com.hqch.simple.resource.cached;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.log4j.Logger;

import com.hqch.simple.exception.BizException;
import com.hqch.simple.exception.DeathException;
import com.hqch.simple.log.LoggerFactory;
import com.hqch.simple.resource.Resource;

public class MemcachedResource {

	private static Logger logger = LoggerFactory.getLogger(MemcachedResource.class);
	
	private static MemcachedClient client;
	
	private static final int TIMEOUT = 60;
	
	/**存入memcached的key带上指定的前缀*/
	private static final String PREFIX_KEY = "simple_";
	
	private Resource res;

	public MemcachedResource (Resource res) throws BizException {
		this.res = res;
		
		if (client != null || res == null) return;
		String serverUrl = res.getHost() + ":" + res.getPort();
		try {
			MemcachedClientBuilder builder = new XMemcachedClientBuilder(
					AddrUtil.getAddresses(serverUrl));
			builder.setCommandFactory(new BinaryCommandFactory());//use binary protocol
			client = builder.build();
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw new DeathException("resource init error:" + res);
		}
	}
	
	private String getKey(String key){
		return PREFIX_KEY + key;
	}
	
	public void save(String key, Object value) {
		save(key, value, TIMEOUT);
	}
	
	public void save(String key, Object value, int timeout) {
		try {
			client.set(getKey(key), timeout, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public <T> T get(String key) {
		T t = null;
		try {
			t = client.get(getKey(key));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return t;
	}
	
	public void remove(String key) {
		try {
			client.delete(getKey(key));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void clearAllByPrefixKey(String prefixKey){
		try {
			String[] keys = allkeys().split("\n");
			for(String key : keys){
				if(key.startsWith(PREFIX_KEY)){
					client.delete(key);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void clearAll(){
		clearAllByPrefixKey(PREFIX_KEY);
	}
	
	
	private String allkeys() {
		StringBuffer r = new StringBuffer();
		try {
			Socket socket = new Socket(res.getHost(), res.getPort());
			PrintWriter os = new PrintWriter(socket.getOutputStream());
			BufferedReader is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			os.println("stats items");
			os.flush();
			String l;
			while (!(l = is.readLine()).equals("END")) {
				r.append(l).append("\n");
			}
			String rr = r.toString();
			Set<String> ids = new HashSet<String>();
			if (rr.length() > 0) {
				r = new StringBuffer();// items
				rr.replace("STAT items", "");
				for (String s : rr.split("\n")) {
					ids.add(s.split(":")[1]);
				}
				if (ids.size() > 0) {
					r = new StringBuffer();//
					for (String s : ids) {
						os.println("stats cachedump " + s + " 0");
						os.flush();
						while (!(l = is.readLine()).equals("END")) {
							r.append(l.split(" ")[1]).append("\n");
						}
					}
				}
			}

			os.close();
			is.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		return r.toString();
	}
}
