package org.ray.rpc.core.client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.ray.rpc.core.protocal.RpcRequest;
import org.ray.rpc.core.protocal.RpcResponse;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Client.java
 * <br><br>
 * @author: ray
 * @date: 2020年12月29日
 */
public interface RpcClient {
	public  <T> T callForObject(RpcRequest request, TypeReference<T> returnType) throws IllegalStateException, InterruptedException, ExecutionException, IOException;
	public  <T> RpcResponse<T> call(RpcRequest request, TypeReference<T> returnType) throws IllegalStateException, InterruptedException, ExecutionException, IOException;
}

