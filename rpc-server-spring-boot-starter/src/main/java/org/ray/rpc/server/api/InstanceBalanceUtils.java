package org.ray.rpc.server.api;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.ray.rpc.core.client.ProviderInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.util.Assert;

/**
 * InstanceBalanceUtils.java
 * <br><br>
 * 实例负载的分配机制
 * @author: ray
 * @date: 2020年12月29日
 */
public class InstanceBalanceUtils {
	/**
	 * 随机算法分配实例的请求
	 * @author ray
	 * @param insList
	 * @return ProviderInstance
	 */
	public static ProviderInstance random(List<ServiceInstance> insList){
		Assert.notNull(insList, "实例集合为空，无法分配");
		int size = insList.size();
		int index = ThreadLocalRandom.current().nextInt(size);
		ServiceInstance instance = insList.get(index);
		ProviderInstance pi = new ProviderInstance();
		pi.setServiceId(instance.getServiceId());
		pi.setHost(instance.getHost());
		pi.setPort(instance.getPort());
		return pi;
	}
}

