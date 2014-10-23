package com.hqch.simple.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.hqch.simple.container.ScheduledTask;

public class ThreadManager {

	private static ScheduledThreadPoolExecutor scheduler = null;
	private final static int MAX_THREAD_COUNT = 10;
	
	private AtomicInteger threadCount = new AtomicInteger();
	
	
	public ThreadManager(){
		scheduler = new ScheduledThreadPoolExecutor(MAX_THREAD_COUNT, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r,"Task-"
						+ threadCount.incrementAndGet());
				return t;
			}
		});
	}
	
	public void registerTask(ScheduledTask task){
		scheduler.scheduleAtFixedRate(task,
				task.getInitialDelay(), task.getRepeat(), task.getTimeUnit());
	}
}
