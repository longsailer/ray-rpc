package org.ray.rpc.consumer.proxy;

import java.lang.reflect.Proxy;

import org.ray.rpc.core.client.RpcClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * ConsumerProxyFactoryBean.java <br>
 * <br>
 * 代理Consumer客户端注入脚本，自动调用服务端
 * @author: ray
 * @date: 2020年12月28日
 */
public class ConsumerProxyFactoryBean implements FactoryBean<Object>, InitializingBean {

	private Class<?> interfaceClass;
	private Object proxy;
	private RpcClient client;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.proxy = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class<?>[] { interfaceClass }, new ConsumerInvocationHandler(client));
	}

	public boolean isSingleton() {
		return true;
	}

	@Override
	public Object getObject() throws Exception {
		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return interfaceClass;
	}

	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public Object getProxy() {
		return proxy;
	}

	public void setProxy(Object proxy) {
		this.proxy = proxy;
	}

	public RpcClient getClient() {
		return client;
	}

	public void setClient(RpcClient client) {
		this.client = client;
	}
}
