package org.ray.rpc.consumer.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

/**
 * ApplicationContextUtil.java
 * <br><br>
 * 获取Spring容器上下文
 * @author: ray
 * @date: 2020年12月23日
 */
public class ApplicationContextAutoWared implements ApplicationContextAware, BeanFactoryAware {
	@Value("${server.port}")
	private int port;
	@Value("${spring.application.name}")
	private String applicationName;
	
	private SpringApplicationContext context = SpringApplicationContext.getInstance();
	
	@Bean
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context.setApplicationContext(applicationContext);
		context.setApplicationName(applicationName);
		context.setPort(port);
	}
	
	@Bean
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		context.setListableBeanFactory((DefaultListableBeanFactory)beanFactory);
	}
}

