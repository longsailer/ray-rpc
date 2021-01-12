package org.ray.rpc.consumer;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ConsumerProperties.java <br>
 * <br>
 * 配置属性类
 * @author: ray
 * @date: 2020年12月25日
 */
@ConfigurationProperties(prefix="rpc.consumer")
public class ConsumerProperties {
	private String consumerLocation;

	public String getConsumerLocation() {
		return consumerLocation;
	}

	public void setConsumerLocation(String consumerLocation) {
		this.consumerLocation = consumerLocation;
	}
}
