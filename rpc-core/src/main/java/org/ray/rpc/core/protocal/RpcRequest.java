package org.ray.rpc.core.protocal;

import java.io.Serializable;

/**
 * RpcRequest.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月29日
 */
public interface RpcRequest extends Serializable{
	
	
	public String getApplicationName();
	
	public String getServiceName();
	
	public String getMethodName();
	
	public Object[] getParamValues();
	
}

