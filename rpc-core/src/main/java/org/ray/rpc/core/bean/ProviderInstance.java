package org.ray.rpc.core.bean;

import java.io.Serializable;

/**
 * ProviderInstance.java
 * <br><br>
 * 服务实例
 * @author: ray
 * @date: 2020年12月29日
 */
public class ProviderInstance implements Serializable {
	/**
	 * serialVersionUID
	 * long
	 */
	private static final long serialVersionUID = 1L;

	private String serviceId;
	
	private String host;
	
	private int port;
	
	private long lastActiveTime;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
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

	public long getLastActiveTime() {
		return lastActiveTime;
	}

	public void setLastActiveTime(long lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}

	@Override
	public int hashCode() {
		StringBuffer id = new StringBuffer();
		return id.append(this.serviceId).append(this.host).append(this.port).toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ProviderInstance){
			ProviderInstance other = (ProviderInstance)obj;
			return this.hashCode() == other.hashCode();
		}
		return false;
	}
}

