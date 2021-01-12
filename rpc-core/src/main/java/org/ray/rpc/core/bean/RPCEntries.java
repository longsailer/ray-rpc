package org.ray.rpc.core.bean;

import java.io.Serializable;

/**
 * RPCEntries.java
 * <br><br>
 * 消息基础数据实体
 * @author: ray
 * @date: 2020年12月23日
 */
public class RPCEntries implements Serializable {

	/**
	 * serialVersionUID
	 * long
	 */
	private static final long serialVersionUID = -1270766980264506547L;
	
	private String requestId;
	
	private String host;
	
	private int port;
	
	private String applicationName;
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}

