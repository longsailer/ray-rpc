package org.ray.rpc.consumer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * Consumer.java
 * <br><br>
 * 消费者注解，用来指定要调用的服务实例名称
 * @author: ray
 * @date: 2020年12月24日
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Consumer {
	/**
	 * 要调用的服务实例名称
	 * @author ray
	 * @return String
	 */
	String appName();
	/**
	 * 要调用的服务名称(一般指类名)
	 * @author ray
	 * @return String
	 */
	@AliasFor("value")
	String serviceName() default "";
	@AliasFor("serviceName")
	String value() default "";
}

