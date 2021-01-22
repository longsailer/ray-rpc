package org.ray.rpc.core.protocal;

import java.io.Serializable;

/**
 * RpcResponse.java
 * <br><br>
 * 远程调的结果接收
 * @author: ray
 * @date: 2020年12月29日
 */
public interface RpcResponse<T> extends Serializable {
	
	public String getRequestId();
	
	public int getStatus();
	
	public String getMsg();

	public T getData();
}

