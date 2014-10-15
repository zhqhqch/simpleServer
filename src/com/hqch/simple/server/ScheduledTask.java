package com.hqch.simple.server;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.hqch.simple.log.LoggerFactory;


public abstract class ScheduledTask implements Runnable {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private ScheduledFuture<?> scheduledFuture;

	private boolean start;
	
	private int initialDelay;
	private int repeat;
	private TimeUnit timeUnit;
	private int runTimes;
	private Date startTime;
	
	public ScheduledTask( int initialDelay,
			int repeat, TimeUnit timeUnit) {
		this.initialDelay = initialDelay;
		this.repeat = repeat;
		this.timeUnit = timeUnit;
	}
	
	public ScheduledFuture<?> getScheduledFuture() {
		return scheduledFuture;
	}

	public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		this.scheduledFuture = scheduledFuture;
	}

	@Override
	public void run() {
		try{
			this.runTimes ++;
			runTask();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	public abstract void runTask();
	
	public void cancelTask(boolean mayInterruptIfRunning){
		scheduledFuture.cancel(mayInterruptIfRunning);
	}

	public boolean isStart() {
		return start;
	}
	
	public Date getStartTime(){
		return this.startTime;
	}

	public int getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public int getRunTimes() {
		return runTimes;
	}

	public void setRunTimes(int runTimes) {
		this.runTimes = runTimes;
	}

	public void setStart(boolean start) {
		this.start = start;
	}
}
