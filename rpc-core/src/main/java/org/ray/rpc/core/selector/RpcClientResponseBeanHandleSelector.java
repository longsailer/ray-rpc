package org.ray.rpc.core.selector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.client.RpcClientTasker;
import org.ray.rpc.core.protocal.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RpcClientHandleSelector.java <br>
 * <br>
 * 客户端的返回结果分拣处理中心
 * @author: ray
 * @date: 2021年1月25日
 */
public class RpcClientResponseBeanHandleSelector {
	private Logger log = LoggerFactory.getLogger(getClass());
	private static RpcClientResponseBeanHandleSelector selector = null;
	private static ReentrantLock lock = new ReentrantLock();

	private Map<String, RpcClientTasker<?>> reqThreadMap = new ConcurrentHashMap<String, RpcClientTasker<?>>();
	private RpcResponseBlockingQueue<String> queue = RpcResponseBlockingQueue.getInstance();

	private RpcClientResponseBeanHandleSelector() {
		this.acceptQueue();
	}

	public static RpcClientResponseBeanHandleSelector build() {
		synchronized (lock) {
			if (selector == null) {
				selector = new RpcClientResponseBeanHandleSelector();
			}
		}
		return selector;
	}

	public <T> void registry(String requestId, RpcClientTasker<T> rpcClientTasker) {
		synchronized (requestId) {
			if (!reqThreadMap.containsKey(requestId)) {
				reqThreadMap.put(requestId, rpcClientTasker);
			}
		}
	}

	private void acceptQueue() {
		Executors.defaultThreadFactory().newThread(new Runnable() {
			public void run() {
				while (true) {
					try {
						RpcResponse<String> response = queue.take();
						String requestId = response.getRequestId();
						if (reqThreadMap.containsKey(requestId)) {
							reqThreadMap.get(requestId).setResponse((RpcResponseBean<String>) response);
							reqThreadMap.remove(requestId);
						} else {
							queue.add(response);
						}
					} catch (InterruptedException e) {
						log.error("结果分拣中心发生错误:", e);
					}
				}
			}
		}).start();
		log.debug("结果分拣中心已启动...");
	}
}
