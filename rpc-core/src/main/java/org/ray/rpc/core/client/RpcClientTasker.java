package org.ray.rpc.core.client;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.ray.rpc.core.RPCResponseFactory;
import org.ray.rpc.core.bean.RpcRequestBean;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.netty.NettyClient;
import org.ray.rpc.core.protocal.RpcRequest;
import org.ray.rpc.core.selector.RpcClientResponseBeanHandleSelector;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * RpcClientTasker.java <br>
 * <br>
 * 封装ReadHandler的任务类，用于发送请求指令，和快速处理异步返回的结果
 * 
 * @author: ray
 * @date: 2021年1月4日
 */
public class RpcClientTasker<T> implements Callable<RpcResponseBean<T>> {
	private RpcRequestBean request;
	private RpcResponseBean<String> response;
	private NettyClient client;
	private TypeReference<T> returnType;
	private RpcClientResponseBeanHandleSelector selector = RpcClientResponseBeanHandleSelector.build();
	private ReentrantLock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	private long maxWaitTime = 15000;

	public RpcClientTasker(RpcRequest request, TypeReference<T> returnType, NettyClient client) {
		this.returnType = returnType;
		this.request = (RpcRequestBean) request;
		this.client = client;
		this.selector.registry(this.request.getRequestId(), this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RpcResponseBean<T> call() throws Exception {
		try {
			lock.lockInterruptibly();
			int reTryCount = 0;
			do {
				try {
					client.writeAndFlush(request);
					break;
				} catch (Exception e) {
					reTryCount++;
					condition.await(50, TimeUnit.MILLISECONDS);
				}
			} while (reTryCount < 3);
			if (reTryCount >= 3) {
				throw new Exception("向服务器发送请求失败.");
			}
			condition.await();
			this.response = this.getResponse();
			if (this.response == null) {
				return RPCResponseFactory.error("请求结果为空 request id : " + request.getRequestId());
			}
			if (String.class.equals(returnType.getType())) {
				return (RpcResponseBean<T>) response;
			}
			return RPCResponseFactory.processResponse(response, returnType);
		} finally {
			if(lock.isLocked())
				lock.unlock();
		}
	}

	public long getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(long maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public RpcResponseBean<String> getResponse() {
		return response;
	}

	public void setResponse(RpcResponseBean<String> response) {
		this.response = response;
		try {
			this.lock.lock();
			this.condition.signal();
		} finally {
			this.lock.unlock();
		}
	}
}
