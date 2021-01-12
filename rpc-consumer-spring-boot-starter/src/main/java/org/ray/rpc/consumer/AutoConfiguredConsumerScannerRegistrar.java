package org.ray.rpc.consumer;

import java.lang.reflect.Field;
import java.util.Set;

import org.ray.rpc.consumer.annotation.Consumer;
import org.ray.rpc.consumer.proxy.ConsumerProxyFactoryBean;
import org.ray.rpc.core.client.RpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.util.StringUtils;

/**
 * RPCConsumerRegistry.java <br>
 * <br>
 * 检查并注册所有调用者的接口
 * 
 * @author: ray
 * @date: 2020年12月24日
 */
public class AutoConfiguredConsumerScannerRegistrar implements BeanDefinitionRegistryPostProcessor {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ConfigurableListableBeanFactory beanFactory;
	private String basePackage;
	private RpcClient client;

	public AutoConfiguredConsumerScannerRegistrar(String basePackage, RpcClient client) {
		this.basePackage = basePackage;
		this.client = client;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true); // 设置过滤条件，这里扫描所有
        Set<BeanDefinition> beanDefinitionSet = scanner.findCandidateComponents(basePackage); // 扫描指定路径下的类
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            log.debug("扫描到的类的名称{}", beanDefinition.getBeanClassName());
            String beanClassName = beanDefinition.getBeanClassName(); // 得到class name
            Class<?> beanClass = null;
            try {
                beanClass = Class.forName(beanClassName); // 得到Class对象
            } catch (ClassNotFoundException e) {
            	log.error("找不到: "+beanClassName, e);
            	continue;
            }
            Field[] fields = beanClass.getDeclaredFields(); // 获得该Class的多有field
            for (Field field : fields) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                Class<?> fieldClass = field.getType(); // 获取该标识下的类的类型，用于生成相应proxy
                Consumer anno = fieldClass.getAnnotation(Consumer.class);
                if (anno != null) {
                    BeanDefinitionHolder holder = createBeanDefinition(fieldClass);
                    log.debug("创建{}的动态代理", fieldClass.getName());
                    // 将代理类的beanDefination注册到容器中
                    BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
                }
            }
        }
	}
	
	 /**
     * 生成fieldClass类型的BeanDefinition
     * @return
     */
    private BeanDefinitionHolder createBeanDefinition(Class<?> fieldClass) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ConsumerProxyFactoryBean.class);
        String className = fieldClass.getName();
        // bean的name首字母小写，spring通过它来注入
        String beanName = StringUtils.uncapitalize(className.substring(className.lastIndexOf('.')+1));
        // 给ProxyFactoryBean字段赋值
        builder.addPropertyValue("interfaceClass", fieldClass);
        builder.addPropertyValue("client", client);
        return new BeanDefinitionHolder(builder.getBeanDefinition(), beanName);
    }

	public ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
}
