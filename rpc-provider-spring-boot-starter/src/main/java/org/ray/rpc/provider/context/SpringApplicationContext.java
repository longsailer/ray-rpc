package org.ray.rpc.provider.context;

import java.util.Map;

import org.ray.rpc.provider.annotation.Provider;
import org.springframework.context.ApplicationContext;

/**
 * SpringApplicationContext.java
 * <br><br>
 * 利用spring容器缓存，提供基于{@link Provider }过滤后的服务库的精准查询
 * @author: ray
 * @date: 2020年12月23日
 */
public class SpringApplicationContext {

	private ApplicationContext applicationContext; 
	
	private static SpringApplicationContext context = new SpringApplicationContext();
	
	private int port;
	private String applicationName;
	
	private SpringApplicationContext(){}
	
	public static SpringApplicationContext getInstance(){
		return context;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
	}
	
	public Object getBean(String clazzName){
		Map<String, Object> map = applicationContext.getBeansWithAnnotation(Provider.class);
		if(map.containsKey(clazzName)){
			return map.get(clazzName);
		}
		return null;
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

