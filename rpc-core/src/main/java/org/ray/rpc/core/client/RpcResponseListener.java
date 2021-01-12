package org.ray.rpc.core.client;


/**
 * ResponseListener.java
 * <br><br>
 * 服务提供者响应请求并返回结果的监听
 * @author: ray
 * @date: 2021年1月4日
 */
public interface RpcResponseListener {
	public void onGetResponse();
	public Object getLock();
}

