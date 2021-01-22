package org.ray.rpc.core.client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.netty.NettyClient;
import org.ray.rpc.core.netty.NettyConnectionPool;
import org.ray.rpc.core.protocal.RpcRequest;
import org.ray.rpc.core.protocal.RpcResponse;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * RpcClient.java <br>
 * <br>
 * RPC客户端API
 * 
 * @author: ray
 * @date: 2020年12月28日
 */
public class TcpRpcClient implements RpcClient {
	private String clusterHost;
	private int clusterPort;
	private NettyConnectionPool factory = NettyConnectionPool.getInstance();
	private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public TcpRpcClient(String clusterHost, int clusterPort) {
		this.clusterHost = clusterHost;
		this.clusterPort = clusterPort;
	}

	public <T> RpcResponse<T> call(RpcRequest request, TypeReference<T> returnType)
			throws IllegalStateException, InterruptedException, ExecutionException, IOException {
		RpcClientTasker<T> tasker = new RpcClientTasker<T>(request, returnType);
		NettyClient client = factory.create(clusterHost, clusterPort, request.getApplicationName());
		client.writeAndFlush(request);
		Future<RpcResponseBean<T>> callResult = executorService.submit(tasker);
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
