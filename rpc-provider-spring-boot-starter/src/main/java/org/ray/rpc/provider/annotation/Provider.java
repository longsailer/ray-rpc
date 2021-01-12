package org.ray.rpc.provider.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

/**
 * Provider.java
 * <br><br>
 * 服务提供者，加此注解才可以对外提供服务
 * @author: ray
 * @date: 2020年12月23日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface Provider{

	/**
	 * 设置提供服务的名称，用于快速查代到该服务，一般默认为接口名称;若遇到两类实现同一接口的，需要进行区分
	 * @author ray
	 * @return String
	 */
	String value();
}

