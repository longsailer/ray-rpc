package org.ray.rpc.core.client;

import java.util.concurrent.Callable;

import org.ray.rpc.core.RPCResponseFactory;
import org.ray.rpc.core.RpcResponseBlockingQueue;
import org.ray.rpc.core.bean.RpcRequestBean;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.protocal.RpcRequest;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * RpcClientTasker.java <br>
 * <br>
 * 封装ReadHandler的任务类，用于发送请求指令，和快速处理异步返回的结果
 * @author: ray
 * @date: 2021年1月4日
 */
public class RpcClientTasker<T> implements Callable<RpcResponseBean<T>> {
	private RpcRequestBean request;
	private RpcResponseBean<String> response;

	private TypeReference<T> returnType;

	private long maxWaitTime = 10000;
	
	private RpcResponseBlockingQueue<String> queue = RpcResponseBlockingQueue.getInstance();
	
	public RpcClientTasker(RpcRequest request, TypeReference<T> returnType) {
		this.returnType = returnType;
		this.request = (RpcRequestBean) request;
	}
	
	public RpcClientTasker<T> ready(){
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RpcResponseBean<T> call() throws Exception {
		try {
			boolean goOn = true;
			long beginTime = System.currentTimeMillis();
			do{
				this.response = (RpcResponseBean<String>) queue.take();
				if(this.response == null){
					return RPCResponseFactory.error("请求结果为空");
				}
				if(this.request.getRequestId().equals(this.response.getRequestId())){
					goOn = false;
					break;
				}else{
					queue.add(response);
				}
				long currentTime = System.currentTimeMillis();
				if(currentTime - beginTime >= maxWaitTime){
					break;
				}
			}while(goOn);
			if (String.class.equals(returnType.getType())) {
				return (RpcResponseBean<T>) response;
			}
			return RPCResponseFactory.processResponse(response, returnType);
		} finally {
		}
	}

	public long getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(long maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}
}
