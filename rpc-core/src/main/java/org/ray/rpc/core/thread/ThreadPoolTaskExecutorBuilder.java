package org.ray.rpc.core.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolTaskExecutorBuilder.java <br>
 * <br>
 * 构建和管理线程池
 * 
 * @author: ray
 * @date: 2020年12月23日
 */
public class ThreadPoolTaskExecutorBuilder {
	private ScheduledThreadPoolExecutor executor;
	private static ThreadPoolTaskExecutorBuilder ttBuilder = new ThreadPoolTaskExecutorBuilder();
	private int corePoolSize = 30;
	private int maxPoolSize = 50;
	private int queueCapacity = Integer.MAX_VALUE;
	private int keepAliveSeconds = 60;
	private volatile boolean initialize = false;

	public static ThreadPoolTaskExecutorBuilder build() {
		return ttBuilder;
	}

	public synchronized ScheduledThreadPoolExecutor defaultPool() {
		if (initialize) {
			return executor;
		}
		return this.definedPool(corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds, "defaultThreadPool_");
	}

	public ScheduledThreadPoolExecutor definedPool(int corePoolSize, int maxPoolSize, int queueCapacity,
			int keepAliveSeconds, String threadNamePrefix) {
		initialize = true;
		executor = new ScheduledThreadPoolExecutor(corePoolSize);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setMaximumPoolSize(maxPoolSize);
		executor.setKeepAliveTime(keepAliveSeconds, TimeUnit.SECONDS);
		return executor;
	}
}
