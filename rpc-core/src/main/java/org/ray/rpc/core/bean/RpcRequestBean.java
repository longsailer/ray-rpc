package org.ray.rpc.core.bean;

import org.ray.rpc.core.protocal.RpcRequest;

/**
 * RPCRequest.java
 * <br><br>
 * 发起调用请求时传输的数据
 * @author: ray
 * @date: 2020年12月23日
 */
public class RpcRequestBean extends RPCEntries implements RpcRequest{
	/**
	 * serialVersionUID
	 * long
	 */
	private static final long serialVersionUID = -4370337961135144647L;

	private String serviceName;
	
	private String methodName;
	
	private Object[] paramValues;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getParamValues() {
		return paramValues;
	}

	public void setParamValues(Object[] paramValues) {
		this.paramValues = paramValues;
	}
}

