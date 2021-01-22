package org.ray.rpc.core.bean;

import org.ray.rpc.core.protocal.RpcResponse;

/**
 * RPCResponse.java
 * <br><br>
 * 调用返回结果数据
 * @author: ray
 * @date: 2020年12月23日
 */
public class RpcResponseBean<T> extends RPCEntries implements RpcResponse<T>{

	/**
	 * serialVersionUID
	 * long
	 */
	private static final long serialVersionUID = 1833264806135956864L;

	private int status;
	private String msg;
	private T data;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}

