package org.ray.rpc.core.client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.netty.NettyClient;
import org.ray.rpc.core.netty.NettyConnectionPool;
import org.ray.rpc.core.protocal.RpcRequest;
import org.ray.rpc.core.protocal.RpcResponse;
import org.ray.rpc.core.thread.ThreadPoolTaskExecutorBuilder;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * RpcClient.java <br>
 * <br>
 * RPC客户端API
 * @author: ray
 * @date: 2020年12月28日
 */
public class TcpRpcClient implements RpcClient {
	private NettyConnectionPool factory = NettyConnectionPool.getInstance();
	private ScheduledThreadPoolExecutor threadPool = ThreadPoolTaskExecutorBuilder.build().defaultPool();

	public <T> RpcResponse<T> call(RpcRequest request, TypeReference<T> returnType)
			throws IllegalStateException, InterruptedException, ExecutionException, IOException {
		NettyClient client = factory.create(request.getApplicationName());
		RpcClientTasker<T> tasker = new RpcClientTasker<T>(request, returnType, client);
		Future<RpcResponseBean<T>> callResult = threadPool.submit(tasker);
		RpcResponseBean<T> response = callResult.get();
		return response;
	}

	public <T> T callForObject(RpcRequest request, TypeReference<T> returnType)
			throws IllegalStateException, InterruptedException, ExecutionException, IOException {
		RpcResponse<T> response = this.call(request, returnType);
		if (response.getStatus() != 200) {
			throw new ExecutionException(new Exception(response.getMsg()));
		}
		return response.getData();
	}
}
