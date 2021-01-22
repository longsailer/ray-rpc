package org.ray.rpc.core.client;

/**
 * RpcGetResponseListener.java <br>
 * <br>
 * 得到返回结果的监听实现
 * 
 * @author: ray
 * @date: 2021年1月4日
 */
public class RpcGetResponseListener implements RpcResponseListener {

	private Object lock;

	public RpcGetResponseListener() {
	}

	public RpcGetResponseListener(Object lock) {
		this.lock = lock;
	}

	public Object getLock() {
		return lock;
	}

	public void onGetResponse() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void setLock(Object lock) {
		this.lock = lock;
	}
}
