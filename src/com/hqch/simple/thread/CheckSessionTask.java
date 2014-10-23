package com.hqch.simple.thread;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.hqch.simple.container.Container;
import com.hqch.simple.container.ScheduledTask;

/**
 * 检测在线session是否断线
 * @author hqch
 *
 */
public class CheckSessionTask extends ScheduledTask {

	private Container container;
	
	public CheckSessionTask(int initialDelay, int repeat, TimeUnit timeUnit) {
		super(initialDelay, repeat, timeUnit);
		container = Container.get();
	}

	@Override
	public void runTask() {
		try{
			List<String> disconnectSessionList = container.checkSession();
			
			container.disconnSession(disconnectSessionList);
		} catch (Exception e){
			logger.error("CheckSessionTask", e);
		}
	}
}
