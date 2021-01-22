package org.ray.rpc.core.ins;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.ray.rpc.core.bean.ProviderInstance;
import org.springframework.util.Assert;

/**
 * ServiceInstanceContext.java
 * <br><br>
 * 实例缓存
 * @author: ray
 * @date: 2021年1月22日
 */
public class ServiceInstanceContext {
	
	private static ServiceInstanceContext context = new ServiceInstanceContext();
	private Map<String, List<ProviderInstance>> serviceCache;
	
	private ServiceInstanceContext(){}
	
	public static ServiceInstanceContext getInstance(){
		return context;
	}

	public Map<String, List<ProviderInstance>> getServiceCache() {
		return serviceCache;
	}

	public void setServiceCache(Map<String, List<ProviderInstance>> serviceCache) {
		this.serviceCache = serviceCache;
	}
	
	public ProviderInstance balance(String appName){
		Assert.hasText(appName, "服务名称不能为空!");
		Assert.notEmpty(serviceCache, "暂无服务");
		return this.random(serviceCache.get(appName.toLowerCase()));
	}
	
	/**
	 * 随机算法分配实例的请求
	 * @author ray
	 * @param insList
	 * @return ProviderInstance
	 */
	private ProviderInstance random(List<ProviderInstance> insList){
		Assert.notNull(insList, "实例集合为空，无法分配");
		int size = insList.size();
		int index = ThreadLocalRandom.current().nextInt(size);
		ProviderInstance instance = insList.get(index);
		return instance;
	}
}

