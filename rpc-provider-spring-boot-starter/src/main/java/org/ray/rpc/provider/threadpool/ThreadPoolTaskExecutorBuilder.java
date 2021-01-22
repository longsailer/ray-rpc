package org.ray.rpc.provider.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ThreadPoolTaskExecutorBuilder.java <br>
 * <br>
 * 构建和管理线程池
 * 
 * @author: ray
 * @date: 2020年12月23日
 */
public class ThreadPoolTaskExecutorBuilder {
	private ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	private static ThreadPoolTaskExecutorBuilder ttBuilder = new ThreadPoolTaskExecutorBuilder();
	private int corePoolSize = 30;
	private int maxPoolSize = 50;
	private int queueCapacity = Integer.MAX_VALUE;
	private int keepAliveSeconds = 60;
	private volatile boolean initialize = false;

	public static ThreadPoolTaskExecutorBuilder build() {
		return ttBuilder;
	}

	public synchronized ThreadPoolTaskExecutor defaultPool() {
		if (initialize && "defaultThreadPool_".equals(executor.getThreadNamePrefix())) {
			return executor;
		}
		return this.definedPool(corePoolSize, maxPoolSize, queueCapacity, keepAliveSeconds, "defaultThreadPool_");
	}

	public ThreadPoolTaskExecutor definedPool(int corePoolSize, int maxPoolSize, int queueCapacity,
			int keepAliveSeconds, String threadNamePrefix) {
		initialize = true;
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.initialize();
		return executor;
	}
}
