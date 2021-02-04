package org.ray.rpc.core.selector;

import java.util.concurrent.LinkedBlockingQueue;

import org.ray.rpc.core.protocal.RpcResponse;

/**
 * RpcResponseBlockingQueue.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2021年1月21日
 */
public class RpcResponseBlockingQueue<T> {
	private static RpcResponseBlockingQueue<String> rrQueue = new RpcResponseBlockingQueue<String>();
	private LinkedBlockingQueue<RpcResponse<T>> queue = new LinkedBlockingQueue<RpcResponse<T>>();
	private RpcResponseBlockingQueue(){}
	
	public static RpcResponseBlockingQueue<String> getInstance(){
		return rrQueue;
	}
	
	public RpcResponse<T> take() throws InterruptedException{
		return queue.take();
	}
	
	public void add(RpcResponse<T> e){
		queue.add(e);
	}
}

