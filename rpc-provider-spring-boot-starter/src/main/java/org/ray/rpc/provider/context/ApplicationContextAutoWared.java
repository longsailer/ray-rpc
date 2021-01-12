package org.ray.rpc.provider.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContextUtil.java
 * <br><br>
 * 获取Spring容器上下文
 * @author: ray
 * @date: 2020年12月23日
 */
@Component
public class ApplicationContextAutoWared implements ApplicationContextAware {
	@Value("${server.port}")
	private int port;
	@Value("${spring.application.name}")
	private String applicationName;
	
	private SpringApplicationContext context = SpringApplicationContext.getInstance();
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context.setApplicationContext(applicationContext);
		context.setApplicationName(applicationName);
		context.setPort(port);
	}
}

