package org.ray.rpc.core.netty;


/**
 * RpcClientReadHandlerFactory.java
 * <br><br>
 * @author: ray
 * @date: 2021年1月4日
 */
public class RpcClientReadHandlerFactory {

	public static <T> RpcClientReadHandler<T> createReadHandler(){
		return new RpcClientReadHandler<T>();
	}
}

