package org.ray.rpc.core.client;

import java.util.concurrent.Callable;

import org.ray.rpc.core.RPCResponseFactory;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.netty.RpcClientReadHandler;
import org.ray.rpc.core.netty.RpcClientReadHandlerFactory;
import org.ray.rpc.core.tcp.RpcRequest;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * RpcClientTasker.java <br>
 * <br>
 * 封装ReadHandler的任务类，用于发送请求指令，和快速处理异步返回的结果
 * @author: ray
 * @date: 2021年1月4日
 */
public class RpcClientTasker<T> implements Callable<RpcResponseBean<T>> {
	
	private RpcClientReadHandler<T> rpcClientReadHandler;

	private RpcRequest request;

	private RpcResponseBean<String> response;

	private TypeReference<T> returnType;

	private long maxWaitTime = 30000;
	
	private final Object responseLock = new Object();
	
	public RpcClientTasker(RpcRequest request, TypeReference<T> returnType) {
		this.rpcClientReadHandler = RpcClientReadHandlerFactory.createReadHandler();
		this.request = request;
		this.returnType = returnType;
	}
	
	public RpcClientTasker<T> ready(){
		this.rpcClientReadHandler.setRequest(request).setListener(new RpcGetResponseListener(responseLock));
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RpcResponseBean<T> call() throws Exception {
		try {
			synchronized (responseLock) {
				if(rpcClientReadHandler.isLoading()){
					responseLock.wait(maxWaitTime);
				}
				this.response = rpcClientReadHandler.getResponse();
			}
			if(this.response == null){
				return RPCResponseFactory.error("请求结果为空");
			}
			if (String.class.equals(returnType.getType())) {
				return (RpcResponseBean<T>) response;
			}
			return RPCResponseFactory.processResponse(response, returnType);
		} finally {
			rpcClientReadHandler.close();
		}
	}

	public long getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(long maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public RpcClientReadHandler<T> getRpcClientReadHandler() {
		return rpcClientReadHandler;
	}

	public void setRpcClientReadHandler(RpcClientReadHandler<T> rpcClientReadHandler) {
		this.rpcClientReadHandler = rpcClientReadHandler;
	}
}
