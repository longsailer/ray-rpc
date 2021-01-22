package org.ray.rpc.consumer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.rmi.ServerException;
import java.util.UUID;

import org.ray.rpc.consumer.annotation.Consumer;
import org.ray.rpc.core.RpcTypeReference;
import org.ray.rpc.core.bean.RpcRequestBean;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.core.client.RpcClient;
import org.ray.rpc.core.protocal.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Handler.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月28日
 */
public class ConsumerInvocationHandler implements InvocationHandler {
	private Logger log = LoggerFactory.getLogger(ConsumerInvocationHandler.class);
	private RpcClient client;
	public ConsumerInvocationHandler(RpcClient client){
		this.client = client;
	}
	public Object invoke(Object target, Method method, Object[] args) throws Throwable {
		RpcResponse<?> response = null;
		try{
			Consumer consumer = method.getDeclaringClass().getAnnotation(Consumer.class);
			String appName = consumer.appName();
			String serviceName = consumer.serviceName();
			if(StringUtils.isEmpty(serviceName)){
				serviceName = method.getDeclaringClass().getSimpleName();
			}
			String methodName = method.getName();
			RpcRequestBean request = new RpcRequestBean();
			request.setApplicationName(appName);
			request.setServiceName(serviceName);
			request.setMethodName(methodName);
			request.setParamValues(args);
			request.setRequestId(UUID.randomUUID().toString());
			log.debug("Send request id is:{}", request.getRequestId());
			response = client.call(request, new RpcTypeReference<RpcResponseBean<Object>>(method.getGenericReturnType()));
			if(response == null){
				throw new NullPointerException("Response is null.");
			}
			log.debug("Received request id is:{}", response.getRequestId());
			if(response.getStatus() != 200){
				throw new ServerException(response.getMsg());
			}
			return response.getData();
		}catch(Exception e){
			log.error("Call remote service happend error.", e);
		}
		return null;
	}
}